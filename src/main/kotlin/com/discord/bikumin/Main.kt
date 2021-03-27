package com.discord.bikumin

import io.ktor.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main(args: Array<String>) {
    Bot(Env.BOT_TOKEN).start()

    embeddedServer(CIO,
        port = Env.PORT,
        host = Env.HOST,
        module = Application::module).start(wait = true)
}
