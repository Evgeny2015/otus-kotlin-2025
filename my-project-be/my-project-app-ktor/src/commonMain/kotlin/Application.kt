package ru.otus.otuskotlin.myproject.app.ktor

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.myproject.api.v2.apiV2Mapper
import ru.otus.otuskotlin.myproject.app.ktor.v2.v2Dev
import ru.otus.otuskotlin.myproject.app.ktor.plugins.initAppSettings

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

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            v2Dev(appSettings)
        }
    }
}
