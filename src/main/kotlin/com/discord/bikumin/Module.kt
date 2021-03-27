package com.discord.bikumin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.module() {
    val bot by lazy {
        Bot(Env.BOT_TOKEN).start()
    }
}

