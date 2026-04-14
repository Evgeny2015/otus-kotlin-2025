package ru.otus.otuskotlin.myproject.bl.stubs

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

fun ICorChainDsl<DevContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора объявления
    """.trimIndent()
    on { stubCase == DevStubs.BAD_ID && state == DevState.RUNNING }
    handle {
        fail(
            DevError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
