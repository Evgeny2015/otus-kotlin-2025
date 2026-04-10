package ru.otus.otuskotlin.myproject.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.myproject.logging.common.IDevLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun devLoggerLogback(logger: Logger): IDevLogWrapper = DevLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun devLoggerLogback(clazz: KClass<*>): IDevLogWrapper = devLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun devLoggerLogback(loggerId: String): IDevLogWrapper = devLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
