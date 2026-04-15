package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.helpers.errorValidation
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail

fun ICorChainDsl<DevContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { devValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
