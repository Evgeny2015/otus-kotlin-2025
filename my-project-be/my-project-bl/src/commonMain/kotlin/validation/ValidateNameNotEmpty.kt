package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.helpers.errorValidation
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail

// смотрим пример COR DSL валидации
fun ICorChainDsl<DevContext>.validateNameNotEmpty(title: String) = worker {
    this.title = title
    on { devValidating.name.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
