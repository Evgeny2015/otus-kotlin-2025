package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == DevState.RUNNING }
    handle {
        devValidated = devValidating
    }
}

fun ICorChainDsl<DevContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == DevState.RUNNING }
    handle {
        devFilterValidated = devFilterValidating
    }
}
