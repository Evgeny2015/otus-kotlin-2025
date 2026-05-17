package ru.otus.otuskotlin.myproject.common.repo

interface IRepoDev {
    suspend fun createDev(rq: DbDevRequest): IDbDevResponse
    suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse
    suspend fun updateDev(rq: DbDevRequest): IDbDevResponse
    suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse
    suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse
    companion object {
        val NONE = object : IRepoDev {
            override suspend fun createDev(rq: DbDevRequest): IDbDevResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateDev(rq: DbDevRequest): IDbDevResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
