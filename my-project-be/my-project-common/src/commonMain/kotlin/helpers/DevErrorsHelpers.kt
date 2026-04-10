package ru.otus.otuskotlin.myproject.common.helpers

import ru.otus.otuskotlin.myproject.common.models.DevError

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
