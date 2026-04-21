package ru.otus.otuskotlin.myproject.bl.general

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.models.DevWorkMode
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.chain

fun ICorChainDsl<DevContext>.stubs(title: String, block: ICorChainDsl<DevContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == DevWorkMode.STUB && state == DevState.RUNNING }
}
