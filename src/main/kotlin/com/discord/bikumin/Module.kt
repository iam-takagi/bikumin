package com.discord.bikumin

import com.discord.bikumin.router.renderingRouter
import com.discord.bikumin.manager.SokujiManager
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.locations.Locations
import io.ktor.websocket.WebSockets

fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Locations)

    val sokujiService = SokujiManager()
    val botService = Bot(System.getenv("TOKEN"), sokujiService, false).start()

    routing {
        renderingRouter(sokujiService)
    }

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

    install(ContentNegotiation) {
        gson {
            // Configure Gson here
        }
    }
}

