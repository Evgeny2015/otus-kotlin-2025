package ru.otus.otuskotlin.myproject.bl.general

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState

fun ICorChainDsl<DevContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == DevState.NONE }
    handle { state = DevState.RUNNING }
}
