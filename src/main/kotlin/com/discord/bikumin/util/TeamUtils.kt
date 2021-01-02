package com.discord.bikumin.util

import com.discord.bikumin.model.MogiPlayer
import java.util.*

object  TeamUtils {

    fun compose(members: List<String?>, makeTeamSize: Int): List<MutableList<String?>> {
        val teams: MutableList<MutableList<String?>> = ArrayList(makeTeamSize)
        for (i in 0 until makeTeamSize) {
            teams.add(ArrayList())
        }
        var i = 0
        Collections.shuffle(members)
        for (player in members) {
            teams[i++ % makeTeamSize].add(player)
        }
        return teams
    }

    fun composeMogi(players: List<MogiPlayer>, makeTeamSize: Int): List<MutableList<MogiPlayer>> {
        val teams: MutableList<MutableList<MogiPlayer>> = ArrayList(makeTeamSize)
        for (i in 0 until makeTeamSize) {
            teams.add(ArrayList())
        }
        var i = 0
        Collections.shuffle(players)
        for (player in players) {
            teams[i++ % makeTeamSize].add(player)
        }
        return teams
    }
}