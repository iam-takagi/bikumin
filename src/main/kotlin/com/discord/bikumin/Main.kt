package com.discord.bikumin

import io.ktor.server.cio.CIO
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main(args: Array<String>) {
    embeddedServer(CIO, commandLineEnvironment(args)).start(wait = true)
}
