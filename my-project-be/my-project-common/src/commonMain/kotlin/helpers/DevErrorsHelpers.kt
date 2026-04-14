package ru.otus.otuskotlin.myproject.common.helpers

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevState

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
