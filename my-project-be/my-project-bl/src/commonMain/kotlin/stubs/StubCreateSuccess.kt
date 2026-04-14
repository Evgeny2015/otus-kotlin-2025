package ru.otus.otuskotlin.myproject.bl.stubs

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.models.DeviceVisibility
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.logging.common.LogLevel
import ru.otus.otuskotlin.myproject.stubs.DevStub

fun ICorChainDsl<DevContext>.stubCreateSuccess(title: String, corSettings: DevCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания устройства
    """.trimIndent()
    on { stubCase == DevStubs.SUCCESS && state == DevState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = DevState.FINISHING
            val stub = DevStub.prepareResult {
                devRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
                devRequest.deviceType.takeIf { it != DevType.NONE }?.also { this.deviceType = it }
                devRequest.visibility.takeIf { it != DeviceVisibility.NONE }?.also { this.visibility = it }
            }
            devResponse = stub
        }
    }
}
