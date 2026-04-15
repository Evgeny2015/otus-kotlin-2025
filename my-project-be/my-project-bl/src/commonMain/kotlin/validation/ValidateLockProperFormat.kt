package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.errorValidation
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevLock
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { devValidating.lock != DevLock.NONE && !devValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = devValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
