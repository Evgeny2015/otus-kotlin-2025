package ru.otus.otuskotlin.myproject.bl.stubs

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

fun ICorChainDsl<DevContext>.stubValidationBadDeviceType(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для типа устройства
    """.trimIndent()
    on { stubCase == DevStubs.BAD_DEVICE_TYPE && state == DevState.RUNNING }
    handle {
        fail(
            DevError(
                group = "validation",
                code = "validation-type",
                field = "deviceType",
                message = "Wrong type field"
            )
        )
    }
}
