package com.discord.bikumin.command

import com.discord.bikumin.service.SokujiService
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

class StopCommand(val sokujiService: SokujiService) : Command() {

    init {
        this.name = "stop"
        this.help = "即時集計を終了します"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            if(sokujiService.removeSokuji(guild.idLong, channel.idLong)){
                reply("即時集計を終了しました")
            }else{
                reply("即時集計は開始されていません")
            }
        }
    }
}