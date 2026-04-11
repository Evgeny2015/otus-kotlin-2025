package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider
import ru.otus.otuskotlin.myproject.logging.socket.SocketLoggerSettings
import ru.otus.otuskotlin.myproject.logging.socket.mpLoggerSocket

fun Application.getSocketLoggerProvider(): DevLoggerProvider {
    val loggerSettings = environment.config.config("ktor.socketLogger").let { conf ->
        SocketLoggerSettings(
            host = conf.propertyOrNull("host")?.getString() ?: "127.0.0.1",
            port = conf.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 9002,
        )
    }
    return DevLoggerProvider { mpLoggerSocket(it, loggerSettings) }
}
