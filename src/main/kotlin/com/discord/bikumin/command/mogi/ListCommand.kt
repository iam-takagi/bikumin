package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.model.MogiState
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

class ListCommand () : Command() {

    init {
        this.name = "list"
        this.aliases = arrayOf("l")
        this.help = "Mogi Participants List"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val mogi = MogiManager.getMogi(guild.idLong, channel.idLong)
            if (mogi != null && mogi.state == MogiState.END || mogi == null) {
                return reply("Mogi hasn't started yet. / 模擬は開始されていません")
            }

            reply(mogi.toString(guild))

        }
    }
}

