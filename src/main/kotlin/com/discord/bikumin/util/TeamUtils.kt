package com.discord.bikumin.util

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

}