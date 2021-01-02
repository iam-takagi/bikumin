package com.discord.bikumin.command.sokuji

import com.discord.bikumin.manager.SokujiManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

class EndwarCommand : Command() {

    init {
        this.name = "endwar"
        this.help = "即時集計を終了します"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            if(SokujiManager.removeSokuji(guild.idLong, channel.idLong)){
                reply("即時集計を終了しました")
            }else{
                reply("即時集計は開始されていません")
            }
        }
    }
}