package com.discord.bikumin.command.mogi

import com.discord.bikumin.manager.MogiManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

class MogiListCommand : Command() {

    init {
        this.name = "mogilist"
        this.aliases = arrayOf("ml")
        this.help = "All Mogi List at the server."
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            reply(buildString {
                append("Mogi List\n")
                val it = MogiManager.getMogiList(guild.idLong).iterator()
                while (it.hasNext()){
                    append(it.next().toString(guild))
                    if(it.hasNext()) {
                        append("\n\n")
                    }
                }
            })
        }
    }
}