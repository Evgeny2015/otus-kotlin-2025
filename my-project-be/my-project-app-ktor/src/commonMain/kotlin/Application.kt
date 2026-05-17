package ru.otus.otuskotlin.myproject.app.ktor

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.myproject.api.v2.apiV2Mapper
import ru.otus.otuskotlin.myproject.app.ktor.v2.v2Dev
import ru.otus.otuskotlin.myproject.app.ktor.plugins.initAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.v2.wsHandlerV2

fun Application.module(
    appSettings: DevAppSettings = initAppSettings()
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }
    // Install ContentNegotiation only if not already installed (e.g., by moduleJvm)
    if (pluginOrNull(ContentNegotiation) == null) {
        install(ContentNegotiation) {
            json(apiV2Mapper)
        }
    }
    install(io.ktor.server.websocket.WebSockets)

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            v2Dev(appSettings)
            webSocket("/ws") {
                wsHandlerV2(appSettings)
            }
        }
    }
}
