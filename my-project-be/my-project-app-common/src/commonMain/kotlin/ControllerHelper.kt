package ru.otus.otuskotlin.myproject.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.myproject.api.log.mapper.toLog
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.asDevError
import ru.otus.otuskotlin.myproject.common.models.DevState
import kotlin.reflect.KClass

suspend inline fun <T> IDevAppSettings.controllerHelper(
    crossinline getRequest: suspend DevContext.() -> Unit,
    crossinline toResponse: suspend DevContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = DevContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = DevState.FAILING
        ctx.errors.add(e.asDevError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
