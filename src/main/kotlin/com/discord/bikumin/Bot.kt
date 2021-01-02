package com.discord.bikumin

import com.discord.bikumin.command.common.ChooseCommand
import com.discord.bikumin.command.common.TagCommand
import com.discord.bikumin.command.common.TeamCommand
import com.discord.bikumin.command.common.KanaTagCommand
import com.discord.bikumin.command.common.NumTagCommand
import com.discord.bikumin.command.mogi.*
import com.discord.bikumin.command.sokuji.*
import com.discord.bikumin.manager.GuildSettingsManager
import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.manager.MongoManager
import com.discord.bikumin.manager.SokujiManager
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.mongodb.client.model.Filters
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*

class Bot (val token: String, val sokujiManager: SokujiManager, val dev: Boolean) {

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
            StartwarCommand(),
            EndwarCommand(),
            RaceCommand(),
            RevertScoreCommand(),
            SetScoreCommand(),
            PenaltyCommand(),
            SetRaceSizeCommand(),
            OverlayCommand(),
            ChooseCommand(),
            KanaTagCommand(),
            NumTagCommand(),
            TagCommand(),
            TeamCommand(),

            CanCommand(),
            DropCommand(),
            EndCommand(),
            EsnCommand(),
            ListCommand(),
            MogiListCommand(),
            StartCommand()
        )

        builder.setHelpWord("bikumin")

        val client = builder.build()
        jda.addEventListener(Listener())
        jda.addEventListener(client)
        return this
    }
}

class Listener : ListenerAdapter() {

    override fun onGuildLeave(event: GuildLeaveEvent) {
        event.apply {
            MongoManager.sokuji_collection.deleteMany(Filters.eq("guildId", guild.idLong))
            MongoManager.guild_settings_collection.deleteMany(Filters.eq("guildId", guild.idLong))
        }
    }

    override fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {
        val mogi = MogiManager.getInMogi(event.member.idLong, event.guild.idLong) ?: return
        if(mogi.drop(event.member.idLong)){
            val c = event.guild.getGuildChannelById(mogi.channelId)?: return
            (c as MessageChannel).sendMessage(event.member.effectiveName + " has dropped mogi.")
        }
    }
}
