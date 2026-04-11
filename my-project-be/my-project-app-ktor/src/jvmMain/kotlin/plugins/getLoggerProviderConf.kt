package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider
import ru.otus.otuskotlin.myproject.logging.jvm.devLoggerLogback
import ru.otus.otuskotlin.myproject.logging.kermit.devLoggerKermit

actual fun Application.getLoggerProviderConf(): DevLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> DevLoggerProvider { devLoggerKermit(it) }
        "socket", "sock" -> getSocketLoggerProvider()
        "logback", null -> DevLoggerProvider { devLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
}
