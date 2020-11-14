package com.discord.bikumin

import com.discord.bikumin.router.renderingRouter
import com.discord.bikumin.service.BotService
import com.discord.bikumin.service.SokujiService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Locations)

    val sokujiService = SokujiService()
    val botService = BotService(System.getenv("TOKEN"), sokujiService,false).start()

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

