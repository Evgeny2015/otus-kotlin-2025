package ru.otus.otuskotlin.myproject.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.myproject.app.spring.base.DevAppSettings
import ru.otus.otuskotlin.myproject.app.spring.base.SpringWsSessionRepo
import ru.otus.otuskotlin.myproject.backend.repository.inmemory.DevRepoStub
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider
import ru.otus.otuskotlin.myproject.logging.jvm.devLoggerLogback
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory

@Suppress("unused")
@Configuration
class DevConfig {
    @Bean
    fun processor(corSettings: DevCorSettings) = DevProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): DevLoggerProvider = DevLoggerProvider { devLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoDev = DevRepoInMemory()

    @Bean
    fun prodRepo(): IRepoDev = DevRepoInMemory()

    @Bean
    fun stubRepo(): IRepoDev = DevRepoStub()

    @Bean
    fun corSettings(): DevCorSettings = DevCorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

    @Bean
    fun appSettings(
        corSettings: DevCorSettings,
        processor: DevProcessor,
    ) = DevAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
