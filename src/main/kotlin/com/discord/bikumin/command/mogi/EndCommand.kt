package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.model.Mogi
import com.discord.bikumin.model.MogiState
import com.discord.bikumin.util.NumberUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.Member
import org.apache.commons.lang3.StringUtils

class EndCommand : Command() {

    init {
        this.name = "end"
        this.help = "End Mogi"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            var mogi = MogiManager.getMogi(guild.idLong, channel.idLong)
            if(mogi == null || mogi.state == MogiState.END) {
                return reply("Mogi hasn't started yet. / 模擬は開始されていません")
            }

            mogi.end(channel, member)
        }
    }
}