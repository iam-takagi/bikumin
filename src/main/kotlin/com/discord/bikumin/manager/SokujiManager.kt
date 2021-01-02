package com.discord.bikumin.manager

import com.discord.bikumin.Bot
import com.discord.bikumin.model.Sokuji
import com.mongodb.client.model.Filters

object SokujiManager {

    fun getSokuji(guildId: Long, channelId: Long) : Sokuji? {
        val document = MongoManager.findSokuji(guildId, channelId) ?: return null
        return Sokuji.fromDocument(document)
    }

    fun getSokujiListByGuildId(guildId: Long) : List<Sokuji>?{
        val toReturn = arrayListOf<Sokuji>()
        val documents = MongoManager.findSokuji(guildId) ?: return null
        documents.forEach { document -> toReturn.add(Sokuji.fromDocument(document))}
        return toReturn
    }

    fun addSokuji(guildId: Long, channelId: Long, teamA: String, teamB: String) : Sokuji? {
        if (getSokuji(guildId, channelId) != null) return null

        return Sokuji(guildId, channelId, mutableListOf(), teamA, teamB, 0, 0, 0, 0, 0, 12).apply {
            MongoManager.replaceSokuji(guildId, channelId, toDocument())
        }
    }

    fun removeSokuji(guildId: Long, channelId: Long) : Boolean{
        val sokuji = getSokuji(guildId, channelId)
        if(sokuji === null)return false
        return MongoManager.sokuji_collection.deleteOne(Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId))).wasAcknowledged()
    }
}