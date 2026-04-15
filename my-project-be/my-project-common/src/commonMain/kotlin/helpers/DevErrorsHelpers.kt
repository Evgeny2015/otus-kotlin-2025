package ru.otus.otuskotlin.myproject.common.helpers

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.logging.common.LogLevel

fun Throwable.asDevError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = DevError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun DevContext.addError(vararg error: DevError) = errors.addAll(error)

inline fun DevContext.fail(error: DevError) {
    addError(error)
    state = DevState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = DevError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)
