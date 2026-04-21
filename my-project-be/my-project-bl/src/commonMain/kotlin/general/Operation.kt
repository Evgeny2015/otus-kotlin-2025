package ru.otus.otuskotlin.myproject.bl.general

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.chain

fun ICorChainDsl<DevContext>.operation(
    title: String,
    command: DevCommand,
    block: ICorChainDsl<DevContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == DevState.RUNNING }
}
