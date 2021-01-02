package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.discord.bikumin.model.MogiState
import com.discord.bikumin.util.NumberUtils
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.Member
import org.apache.commons.lang3.StringUtils

class CanCommand () : Command() {

    init {
        this.name = "c"
        this.arguments = "<mention (optional)>"
        this.help = "Can Mogi"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            if (args.isNotEmpty()) {

                //模擬が開始されていない場合
                val mogi = MogiManager.getMogi(guild.idLong, channel.idLong)
                if (mogi != null && mogi.state == MogiState.END || mogi == null) {
                    return reply("Mogi hasn't started yet. / 模擬は開始されていません")
                }

                //既に他の模擬に参加している場合
                val inMogi = MogiManager.getInMogi(member.idLong, guild.idLong)
                if (inMogi != null) {
                    return reply("You have already added this server's mogi in <#${inMogi.channelId}> / あなたは既にこのサーバーの模擬に参加しています。")
                }

                // 既に始まってる場合
                if (mogi.state == MogiState.ACTIVE) {
                    return reply("Mogi has already started. / 模擬は既に始まっています")
                }

                val members = mutableListOf<Member>()

                // FFA
                if (mogi.size == 1) {
                    mogi.can(guild, channel, member, members)
                } else {

                    //それ以外
                    args.forEach {
                        var id = it.replace("<", "").replace("@", "").replace(">", "").trim()
                        if (id.startsWith('!')) {
                            id = id.replace("!", "").trim()
                        }
                        if (!NumberUtils.isLong(id)) {
                            return reply("Please type mention(s) / メンションを入力してください")
                        }

                        val member = guild.getMemberById(id) ?: return reply("No members found　/ メンバーが見つかりません")

                        //既に他の模擬に参加している場合
                        val inm = MogiManager.getInMogi(id.toLong(), guild.idLong)
                        if (inm != null) {
                            return reply("You have already added this server's mogi in <#${inm.channelId}> / あなたは既にこのサーバーの模擬に参加しています。")
                        }

                        members.add(member)
                    }

                    mogi.can(guild, channel, member, members)
                }

                if(mogi.sumByParticipants() == 12) {
                    mogi.active(guild, channel)
                }
            }
        }
    }
}