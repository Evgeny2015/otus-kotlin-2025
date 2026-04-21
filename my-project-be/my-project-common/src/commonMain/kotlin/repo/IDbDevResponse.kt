package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevError

sealed interface IDbDevResponse: IDbResponse<DevAd>

data class DbDevResponseOk(
    val data: DevAd
): IDbDevResponse

data class DbDevResponseErr(
    val errors: List<DevError> = emptyList()
): IDbDevResponse {
    constructor(err: DevError): this(listOf(err))
}

data class DbDevResponseErrWithData(
    val data: DevAd,
    val errors: List<DevError> = emptyList()
): IDbDevResponse {
    constructor(ad: DevAd, err: DevError): this(ad, listOf(err))
}
