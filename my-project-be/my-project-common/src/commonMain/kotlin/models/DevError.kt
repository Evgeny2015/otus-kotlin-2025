package ru.otus.otuskotlin.myproject.common.models
import ru.otus.otuskotlin.myproject.logging.common.LogLevel

data class DevError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
