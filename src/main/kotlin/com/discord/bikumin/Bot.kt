package com.discord.bikumin

import com.discord.bikumin.command.ChooseCommand
import com.discord.bikumin.command.TagCommand
import com.discord.bikumin.command.TeamCommand
import com.discord.bikumin.command.KanaTagCommand
import com.discord.bikumin.command.NumTagCommand
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import net.dv8tion.jda.api.*
import java.util.*

class Bot (val token: String) {

    companion object {
        @JvmStatic
        lateinit var instance: Bot

        @JvmStatic
        lateinit var random: Random
    }

    lateinit var jda: JDA
    val eventWaiter = EventWaiter()

    fun start() : Bot {
        instance = this
        random = Random()
        jda = JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).build()

        val builder = CommandClientBuilder()

        builder.setOwnerId("695218967173922866")
        builder.setPrefix("_")

        builder.addCommands(
            ChooseCommand(),
            KanaTagCommand(),
            NumTagCommand(),
            TagCommand(),
            TeamCommand(),
        )

        builder.setHelpWord("bikumin")

        val client = builder.build()
        jda.addEventListener(client)
        return this
    }
}
