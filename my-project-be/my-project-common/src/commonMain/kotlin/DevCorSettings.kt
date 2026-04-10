package ru.otus.otuskotlin.myproject.common
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider

data class DevCorSettings (
    val loggerProvider: DevLoggerProvider = DevLoggerProvider(),
) {
    companion object {
        val NONE = DevCorSettings()
    }
}