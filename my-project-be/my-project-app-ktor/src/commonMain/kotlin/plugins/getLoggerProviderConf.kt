package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider

expect fun Application.getLoggerProviderConf(): DevLoggerProvider
