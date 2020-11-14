package com.discord.bikumin.service

import com.discord.bikumin.command.*
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.mongodb.client.model.Filters
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity.watching
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class BotService (val token: String, val sokujiService: SokujiService, val dev: Boolean) {

    companion object {
        @JvmStatic
        lateinit var instance: BotService

        @JvmStatic
        lateinit var mongoService: MongoService

        @JvmStatic
        lateinit var random: Random
    }

    lateinit var jda: JDA
    lateinit var settingsService: GuildSettingsService
    val eventWaiter = EventWaiter()

    fun start() : BotService {
        instance = this
        random = Random()
        mongoService = MongoService(dev)
        settingsService = GuildSettingsService(mongoService)
        jda = JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).build()

        val builder = CommandClientBuilder()

        builder.setOwnerId("695218967173922866")
        builder.setPrefix("_")

        builder.addCommands(
            StartCommand(sokujiService),
            StopCommand(sokujiService),
            RaceCommand(sokujiService),
            RevertScoreCommand(sokujiService),
            SetScoreCommand(sokujiService),
            PenaltyCommand(sokujiService),
            SetRaceSizeCommand(sokujiService),
            OverlayCommand(sokujiService),
            ChooseCommand(),
            KanaTagCommand(),
            NumTagCommand(),
            TagCommand(),
            TeamCommand()
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
            BotService.mongoService.sokuji_collection.deleteMany(Filters.eq("guildId", guild.idLong))
            BotService.mongoService.guild_settings_collection.deleteMany(Filters.eq("guildId", guild.idLong))
        }
    }
}
