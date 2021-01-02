package com.discord.bikumin.manager

import com.discord.bikumin.model.Mogi

object MogiManager {

    val mogiList = mutableListOf<Mogi>()

    fun getMogi(guildId: Long, channelId: Long) : Mogi? {
        return mogiList.find { it.guildId == guildId && it.channelId == channelId }
    }

    fun getInMogi(userId: Long, guildId: Long) : Mogi? {
        getMogiList(guildId).forEach { mogi ->
            mogi.participants?.forEach {
                if(it.containsPlayer(userId)){
                    return mogi
                }
            }
        }
        return null
    }

    fun getMogiList(guildId: Long) : List<Mogi>{
        return mogiList.filter { it.guildId == guildId }
    }
}