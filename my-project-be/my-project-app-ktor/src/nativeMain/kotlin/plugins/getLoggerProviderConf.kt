package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider
import ru.otus.otuskotlin.myproject.logging.kermit.devLoggerKermit

actual fun Application.getLoggerProviderConf(): DevLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "socket", "sock" -> getSocketLoggerProvider()
        "kmp", null -> DevLoggerProvider { devLoggerKermit(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and socket")
    }
