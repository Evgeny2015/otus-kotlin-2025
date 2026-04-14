package ru.otus.otuskotlin.myproject.logging.socket

import kotlinx.serialization.Serializable
import ru.otus.otuskotlin.myproject.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
