package com.discord.bikumin.command

import com.discord.bikumin.Bot
import com.discord.bikumin.Env
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import org.apache.commons.lang3.StringUtils
import java.awt.Color

class ChooseCommand : Command() {

    init {
        this.name = "choose"
        this.help = "Chooseします (20まで)"
        this.arguments = "<選択肢1> <選択肢2> <選択肢3>..."
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)

            if(args.size > 20){
                return reply(EmbedBuilder().apply {
                    setColor(Env.EMBED_COLOR)
                    setTitle("Error")
                    setDescription("上限を超えています (20まで)")
                }.build())
            }

            reply(args[Bot.random.nextInt(args.size)])
        }
    }
}