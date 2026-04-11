package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings

fun Application.initAppSettings(): DevAppSettings {
    val corSettings = DevCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return DevAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DevProcessor(corSettings),
    )
}
