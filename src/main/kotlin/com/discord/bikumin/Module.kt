package com.discord.bikumin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Post)
        method(HttpMethod.Patch)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
        anyHost()
    }
}

