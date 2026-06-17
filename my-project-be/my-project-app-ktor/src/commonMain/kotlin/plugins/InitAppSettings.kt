package ru.otus.otuskotlin.myproject.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.myproject.backend.repository.inmemory.DevRepoStub
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory

fun Application.initAppSettings(): DevAppSettings {
    val corSettings = DevCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
        repoTest = DevRepoInMemory(),
        repoProd = DevRepoInMemory(),
        repoStub = DevRepoStub(),
    )
    return DevAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DevProcessor(corSettings),
    )
}
