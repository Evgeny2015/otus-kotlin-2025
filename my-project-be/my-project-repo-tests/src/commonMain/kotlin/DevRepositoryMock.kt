package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.repo.*

class DevRepositoryMock(
    private val invokeCreateDev: (DbDevRequest) -> IDbDevResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadDev: (DbDevIdRequest) -> IDbDevResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateDev: (DbDevRequest) -> IDbDevResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteDev: (DbDevIdRequest) -> IDbDevResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchDev: (DbDevFilterRequest) -> IDbDevsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoDev {
    override suspend fun createDev(rq: DbDevRequest): IDbDevResponse {
        return invokeCreateDev(rq)
    }

    override suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse {
        return invokeReadDev(rq)
    }

    override suspend fun updateDev(rq: DbDevRequest): IDbDevResponse {
        return invokeUpdateDev(rq)
    }

    override suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse {
        return invokeDeleteDev(rq)
    }

    override suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse {
        return invokeSearchDev(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbDevResponseOk(DevAd())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbDevsResponseOk(emptyList())
    }
}
