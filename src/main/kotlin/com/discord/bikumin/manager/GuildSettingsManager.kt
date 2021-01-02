package com.discord.bikumin.manager

import com.discord.bikumin.model.GuildSettings

object GuildSettingsManager {

    fun getGuildSettings(guildId: Long) : GuildSettings {
        MongoManager.apply {
            val doc = findGuildSettings(guildId)
            if(doc == null){
                val settings = GuildSettings(guildId)
                replaceGuildSettings(guildId, settings.toDocument())
                return settings
            }
            return GuildSettings.fromDocument(doc)
        }
    }
}