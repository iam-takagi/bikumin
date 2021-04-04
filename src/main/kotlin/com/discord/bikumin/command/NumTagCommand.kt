package com.discord.bikumin.command

import com.discord.bikumin.Env
import com.discord.bikumin.util.NumberUtils
import com.discord.bikumin.util.TagUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import org.apache.commons.lang3.StringUtils
import java.awt.Color

class NumTagCommand : Command() {

    init {
        this.name = "numtag"
        this.help = "ランダムな数字でタグを決めます"
        this.arguments = "<文字数>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)

            var tagLength: Int = 0
            if (args.isNotEmpty()) {

                if (!NumberUtils.isInteger(args[0])) {
                    return reply(EmbedBuilder().apply {
                        setColor(Color.YELLOW)
                        setTitle("Error")
                        setDescription("文字数は1~10で指定してください\n``_tag <文字数>``")
                    }.build())
                }

                tagLength = args[0].toInt()

                if (tagLength > 10 || tagLength < 1) {
                    return reply(EmbedBuilder().apply {
                        setColor(Color.YELLOW)
                        setTitle("Error")
                        setDescription("文字数は1~10で指定してください\n``_tag <文字数>``")
                    }.build())
                }

                //結果出力
                reply("タグ: " + TagUtils.randomNum(tagLength))
            }
        }
    }
}