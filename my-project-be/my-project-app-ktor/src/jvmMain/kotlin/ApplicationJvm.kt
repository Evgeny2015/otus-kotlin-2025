package ru.otus.otuskotlin.myproject.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.EngineMain
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.webSocket
import org.slf4j.event.Level
import ru.otus.otuskotlin.myproject.app.ktor.v1.v1Dev
import ru.otus.otuskotlin.myproject.app.ktor.plugins.initAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.v1.wsHandlerV1


fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(
    appSettings: DevAppSettings = initAppSettings(),
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }
    // ContentNegotiation is installed in module() with Kotlinx JSON for v2.
    // Both v1 and v2 handle serialization manually in their ControllerHelper.
    module(appSettings)

    routing {
        route("v1") {
            v1Dev(appSettings)
            webSocket("/ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}
