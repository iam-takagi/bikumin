package com.discord.bikumin.model

import com.discord.bikumin.util.TeamUtils
import com.sun.org.apache.xpath.internal.operations.Bool
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import java.util.*

class MogiPlayer (val userId: Long) {

    fun getName(guild: Guild) : String? = guild.getMemberById(userId)?.effectiveName
}

open class MogiParticipant<T: MogiPlayer?> (val sender: T) {
    open val players: MutableList<T> get() = Collections.singletonList(sender)

    open fun containsPlayer(userId: Long?): Boolean {
        return sender?.userId == userId
    }

    open fun conjoinedNames(guild: Guild): String? {
        return sender?.getName(guild)
    }
}

class TeamMogiParticipant<T : MogiPlayer?>(var t: T) : MogiParticipant<T>(t) {

    override val players: MutableList<T>

    init {
        players = mutableListOf()
        players.add(t)
    }

    fun getMogiPlayer(userId: Long) : T? {
        return players.find { it?.userId == userId}
    }

    fun addPlayers(players: List<T>) : TeamMogiParticipant<T> {
        players.forEach {
            this.players.add(it)
        }
        return this
    }

    override fun containsPlayer(userId: Long?): Boolean {
        for (player in players) {
            return (player?.userId == userId)
        }
        return false
    }

    override fun conjoinedNames(guild: Guild): String? =
        buildString {
            val it = players.iterator()
            while (it.hasNext()) {
                val p = it.next()
                append(p?.getName(guild))
                if (it.hasNext()) {
                    append(",")
                }
            }
        }
}

enum class MogiState {
    QUEUE,
    ACTIVE,
    END
}

class Mogi(val guildId: Long,
           val channelId: Long,
           var size: Int = 1,
           var state: MogiState? = MogiState.QUEUE,
           val participants: MutableList<MogiParticipant<MogiPlayer>>? = mutableListOf()
) {

    fun isFull() : Boolean {
        return sumByParticipants() == ((12 + size - 1 ) / size)
    }

    fun can(guild: Guild, channel: MessageChannel, sender: Member, members: List<Member>) {
        // 形式人数超過
        if (members.size > size) {
            channel.sendMessage(sender.asMention + " This mogi size is " + getSizeName() + " / この模擬の形式は " + getSizeName() + " です")
            return
        }

        // 12人超過
        val sum = sumByParticipants()
        if (members.size + sum > 12) {
            val over = members.size + sum
            channel.sendMessage(sender.asMention + " " + over + " players over / " + over + "人 超過しています")
                .queue()
            return
        }

        // OK
        var participant: MogiParticipant<MogiPlayer>? = null
        val s = MogiPlayer(sender.idLong)
        // FFA
        if (size == 1) {
            participant = MogiParticipant(s)
        }

        //FFA以外
        val players = mutableListOf<MogiPlayer>()
        members.forEach {
            players.add(MogiPlayer(it.idLong))
        }

        TeamMogiParticipant(s).addPlayers(players)
        participants?.add(participant!!)

        channel.sendMessage(participant!!.conjoinedNames(guild) + " added to mogi / 模擬に参加しました").queue()
    }

    fun drop(userId: Long) : Boolean {
        var participant = getParticipant(userId) ?: return false

        if(size == 1) {
            participants?.remove(participant)
            return true
        }

        (participant as TeamMogiParticipant<MogiPlayer>).players.remove(participant.getMogiPlayer(userId))
        return true
    }

    fun active(guild: Guild, channel: MessageChannel) {
        state = MogiState.ACTIVE

        if(size == 1) return channel.sendMessage("Mogi has changed to active!").queue()

        //チーム形式で個人挙手したプレイヤーを組み分ける
        val players = mutableListOf<MogiPlayer>()
        participants?.filter { (it as TeamMogiParticipant<MogiPlayer>).players.size == 1 }?.forEach {
            players.add(it.sender)
            participants.remove(it)
        }
        val makeTeamSize = (players.size + size - 1 ) / size
        val composed = TeamUtils.composeMogi(players, makeTeamSize)
        composed.forEach {
            val t = TeamMogiParticipant(it.first())
            t.addPlayers(it.slice(IntRange(1, size)))
            participants?.add(t)
        }

        channel.sendMessage(toString(guild))
    }

    fun esn(channel: MessageChannel, sender: Member) {
        participants?.clear()
        state = MogiState.QUEUE
        channel.sendMessage(sender.effectiveName + " has started ${getSizeName()} mogi / 模擬が開始されました").queue()
    }

    fun start(channel: MessageChannel, sender: Member) {
        participants?.clear()
        state = MogiState.QUEUE
        channel.sendMessage(sender.effectiveName + " has started ${getSizeName()} mogi / 模擬が開始されました").queue()
    }

    fun end(channel: MessageChannel, sender: Member) {
        participants?.clear()
        state = MogiState.END
        channel.sendMessage(sender.effectiveName + " has ended mogi. / 模擬が終了しました").queue()
    }

    fun getParticipant(userId: Long) : MogiParticipant<MogiPlayer>? {
        return participants?.find {
            it.containsPlayer(userId)
        }
    }

    fun getSizeName() : String {
        return if(size == 1) "FFA" else "${size}v${size}"
    }

    fun sumByParticipants() : Int {
        return participants?.sumBy {
            it.players.size
        }!!
    }

    fun toString(guild: Guild) : String {
       return buildString {
            append("<#" + channelId + "> - ${participants?.size}/12 - ${getSizeName()} - $state\n")

            val it = participants?.iterator()
            var i = 0
            if (it != null) {
                while (it.hasNext()) {
                    append("`${i}. `" + it.next().conjoinedNames(guild))

                    if (it.hasNext()) {
                        append("\n")
                    }

                    i++
                }
            }
        }
    }
}