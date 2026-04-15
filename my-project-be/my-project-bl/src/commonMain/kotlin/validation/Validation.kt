package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.chain

fun ICorChainDsl<DevContext>.validation(block: ICorChainDsl<DevContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == DevState.RUNNING }
}
