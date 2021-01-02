package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.model.MogiState
import com.discord.bikumin.util.NumberUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.Member
import org.apache.commons.lang3.StringUtils

class DropCommand () : Command() {

    init {
        this.name = "d"
        this.help = "Drop Mogi"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            //模擬が開始されていない場合
            val mogi = MogiManager.getMogi(guild.idLong, channel.idLong)

            if (mogi != null && mogi.state == MogiState.END || mogi == null) {
                return reply("Mogi hasn't started yet. / 模擬は開始されていません")
            }

            val inMogi = MogiManager.getInMogi(member.idLong, guild.idLong) ?: return reply("You are not in a mogi / あなたは模擬に参加していません")

            if(inMogi.state == MogiState.ACTIVE) return reply("Mogi has already started. / 模擬は既に始まっています")

            if(inMogi.state == MogiState.END) return reply("Mogi is ended / 模擬は終了しています")

            if(inMogi.drop(member.idLong)){
                reply(member.effectiveName + " has dropped mogi.")
            }
        }
    }
}