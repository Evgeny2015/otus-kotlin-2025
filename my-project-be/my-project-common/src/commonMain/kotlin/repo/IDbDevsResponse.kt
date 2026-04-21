package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevError

sealed interface IDbDevsResponse: IDbResponse<List<DevAd>>

data class DbDevsResponseOk(
    val data: List<DevAd>
): IDbDevsResponse

@Suppress("unused")
data class DbDevsResponseErr(
    val errors: List<DevError> = emptyList()
): IDbDevsResponse {
    constructor(err: DevError): this(listOf(err))
}
