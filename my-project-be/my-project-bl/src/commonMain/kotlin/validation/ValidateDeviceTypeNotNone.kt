package ru.otus.otuskotlin.myproject.biz.validation

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.helpers.errorValidation
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevType

fun ICorChainDsl<DevContext>.validateDeviceTypeNotNone(title: String) = worker {
    this.title = title
    on { devValidating.deviceType == DevType.NONE }
    handle {
        fail(
            errorValidation(
            field = "deviceType",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
