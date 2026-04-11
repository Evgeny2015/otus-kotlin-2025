package ru.otus.otuskotlin.myproject.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import ru.otus.otuskotlin.myproject.logging.common.IDevLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun devLoggerKermit(loggerId: String): IDevLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return DevLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun devLoggerKermit(cls: KClass<*>): IDevLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return DevLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}
