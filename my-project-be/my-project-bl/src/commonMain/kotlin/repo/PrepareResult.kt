package ru.otus.otuskotlin.myproject.bl.repo

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.models.DevWorkMode
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != DevWorkMode.STUB }
    handle {
        devResponse = devRepoDone
        devsResponse = devsRepoDone
        state = when (val st = state) {
            DevState.RUNNING -> DevState.FINISHING
            else -> st
        }
    }
}
