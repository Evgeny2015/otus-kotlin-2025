package ru.otus.otuskotlin.myproject.bl.stubs

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.logging.common.LogLevel
import ru.otus.otuskotlin.myproject.stubs.DevStub

fun ICorChainDsl<DevContext>.stubSearchSuccess(title: String, corSettings: DevCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска устройств
    """.trimIndent()
    on { stubCase == DevStubs.SUCCESS && state == DevState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = DevState.FINISHING
            devsResponse.addAll(DevStub.prepareSearchList(devFilterRequest.searchString, DevType.DEVICE))
        }
    }
}
