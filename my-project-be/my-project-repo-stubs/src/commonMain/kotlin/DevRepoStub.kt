package ru.otus.otuskotlin.myproject.backend.repository.inmemory

import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.repo.*
import ru.otus.otuskotlin.myproject.stubs.DevStub

class DevRepoStub() : IRepoDev {
    override suspend fun createDev(rq: DbDevRequest): IDbDevResponse {
        return DbDevResponseOk(
            data = DevStub.get(),
        )
    }

    override suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse {
        return DbDevResponseOk(
            data = DevStub.get(),
        )
    }

    override suspend fun updateDev(rq: DbDevRequest): IDbDevResponse {
        return DbDevResponseOk(
            data = DevStub.get(),
        )
    }

    override suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse {
        return DbDevResponseOk(
            data = DevStub.get(),
        )
    }

    override suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse {
        return DbAdsResponseOk(
            data = DevStub.prepareSearchList(filter = "", DevType.DEVICE),
        )
    }
}
