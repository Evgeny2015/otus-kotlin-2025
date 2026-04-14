package ru.otus.otuskotlin.myproject.bl.stubs

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

fun ICorChainDsl<DevContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == DevStubs.DB_ERROR && state == DevState.RUNNING }
    handle {
        fail(
            DevError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
