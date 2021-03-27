package com.discord.bikumin.command

import com.discord.bikumin.Env
import com.discord.bikumin.util.NumberUtils
import com.discord.bikumin.util.TagUtils
import com.discord.bikumin.util.TeamUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import org.apache.commons.lang3.StringUtils

class TeamCommand : Command() {

    init {
        this.name = "t"
        this.help = "チーム分けをします\nsize\n1: FFA\n2: 2v2\n3: 3v3\n4: 4v4\n5: 5v5\n6: 6v6"
        this.arguments = "<size> <name1> <name2> <name3>..."
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)

            var size = 0
            if (args.isNotEmpty()) {

                if(!NumberUtils.isInteger(args[0])){
                    return reply(EmbedBuilder().apply {
                        setColor(Env.EMBED_COLOR)
                        setTitle("Error")
                        setDescription("チーム形式は1~6で指定してください\n``_t <チーム形式> <name1> <name2> <name3>...``\n" +
                                "チーム形式\n" +
                                "1: FFA\n" +
                                "2: 2v2\n" +
                                "3: 3v3\n" +
                                "4: 4v4\n" +
                                "5: 5v5\n" +
                                "6: 6v6")
                    }.build())
                }

                size = args[0].toInt()

                if(size > 6 || size < 1){
                    return reply(EmbedBuilder().apply {
                        setColor(Env.EMBED_COLOR)
                        setTitle("Error")
                        setDescription("チーム形式は2~6で指定してください\n``_t <チーム形式> <name1> <name2> <name3>...``\n" +
                                "チーム形式\n" +
                                "1: FFA\n" +
                                "2: 2v2\n" +
                                "3: 3v3\n" +
                                "4: 4v4\n" +
                                "5: 5v5\n" +
                                "6: 6v6")
                    }.build())
                }
            }

            //名前格納用
            val players = arrayListOf<String>()

            //格納処理
            val copy = args.toMutableList()
            copy.removeAt(0)
            for (element in copy) {
                players.add(element)
            }

            if (players.size > 12) {
                return reply(EmbedBuilder().apply {
                    setColor(Env.EMBED_COLOR)
                    setTitle("Error")
                    setDescription("12人を超えています")
                }.build())
            }


            val makeTeamSize = (players.size + size - 1 ) / size

            val teams = TeamUtils.compose(players, makeTeamSize)

            //結果出力
            reply(EmbedBuilder().apply {
                var type = ""
                if(size == 1){
                    type = ""
                } else {
                    type = "(" + size + "v" + size + ")"
                }

                setColor(Env.EMBED_COLOR)
                setTitle("Teams: " + type)
                var i = 0
                teams.filter { team -> team.isNotEmpty() }.forEach{ team ->
                    addField(TagUtils.getAlphabet(i).toUpperCase() + " (" + team.size + ")", team.toString().replace("[", "").replace("]", ""), true)
                    i++
                }
            }.build())
        }
    }
}