package ru.otus.otuskotlin.myproject.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider
import ru.otus.otuskotlin.myproject.logging.jvm.devLoggerLogback

@Suppress("unused")
@Configuration
class DevConfig {
    @Bean
    fun processor(corSettings: DevCorSettings) = DevProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): DevLoggerProvider = DevLoggerProvider { devLoggerLogback(it) }

    @Bean
    fun corSettings(): DevCorSettings = DevCorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: DevCorSettings,
        processor: DevProcessor,
    ) = DevAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
