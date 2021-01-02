package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.model.Mogi
import com.discord.bikumin.model.MogiState
import com.discord.bikumin.util.NumberUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import org.apache.commons.lang3.StringUtils

class StartCommand : Command() {

    init {
        this.name = "start"
        this.help = "Start Mogi"
        this.arguments = "<size>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            if(args.isNotEmpty()) {

                if(!NumberUtils.isInteger(args[0])) return reply("Please type integer. <size> 1: FFA, 2: 2v2, 3: 3v3, 4: 4v4, 6: 6v6")

                val size = args[0].toInt()

                if(size != 1 && size != 2 && size != 3 && size != 4 && size != 6) {
                    return reply("<size> 1: FFA, 2: 2v2, 3: 3v3, 4: 4v4, 6: 6v6")
                }

                //募集中 || 満員
                var mogi = MogiManager.getMogi(guild.idLong, channel.idLong)
                if(mogi != null && mogi.state == MogiState.QUEUE || mogi != null && mogi.state == MogiState.ACTIVE) {
                    return reply("Already Started / 模擬が既に開始されています")
                }

                if(mogi == null){
                    mogi = Mogi(guild.idLong, channel.idLong)
                    MogiManager.mogiList.add(mogi)
                }

                mogi.size = size
                mogi.start(channel, member)
            }
        }
    }
}