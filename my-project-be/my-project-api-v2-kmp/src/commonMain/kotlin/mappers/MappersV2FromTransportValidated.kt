package ru.otus.otuskotlin.myproject.api.v2.mappers

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

// Демонстрация форматной валидации в мапере
private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T,E>
private data class Err<T,E>(val errors: List<E>) : Result<T,E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T,E>.getOrExec(default: T, block: (Err<T,E>) -> Unit = {}): T = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T,E>.getOrNull(block: (Err<T,E>) -> Unit = {}): T? = when (this) {
    is Ok<T,E> -> this.value
    is Err<T,E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<DevStubs,DevError> = when (this) {
    "success" -> Ok(DevStubs.SUCCESS)
    "notFound" -> Ok(DevStubs.NOT_FOUND)
    "badId" -> Ok(DevStubs.BAD_ID)
    "badName" -> Ok(DevStubs.BAD_NAME)
    "badVisibility" -> Ok(DevStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(DevStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(DevStubs.BAD_SEARCH_STRING)
    null -> Ok(DevStubs.NONE)
    else -> Err(
        DevError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun DevContext.fromTransportValidated(request: DevCreateRequest) {
    command = DevCommand.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(DevStubs.NONE) { err: Err<DevStubs,DevError> ->
            errors.addAll(err.errors)
            state = DevState.FAILING
        }
}
