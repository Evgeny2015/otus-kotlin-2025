package ru.otus.otuskotlin.myproject.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.repo.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DevRepositoryMockTest {
    private val repo = DevRepositoryMock(
        invokeCreateDev = { DbDevResponseOk(DevStub.prepareResult { name = "create" }) },
        invokeReadDev = { DbDevResponseOk(DevStub.prepareResult { name = "read" }) },
        invokeUpdateDev = { DbDevResponseOk(DevStub.prepareResult { name = "update" }) },
        invokeDeleteDev = { DbDevResponseOk(DevStub.prepareResult { name = "delete" }) },
        invokeSearchDev = { DbAdsResponseOk(listOf(DevStub.prepareResult { name = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createDev(DbDevRequest(DevAd()))
        assertIs<DbDevResponseOk>(result)
        assertEquals("create", result.data.name)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readDev(DbDevIdRequest(DevAd()))
        assertIs<DbDevResponseOk>(result)
        assertEquals("read", result.data.name)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateDev(DbDevRequest(DevAd()))
        assertIs<DbDevResponseOk>(result)
        assertEquals("update", result.data.name)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteDev(DbDevIdRequest(DevAd()))
        assertIs<DbDevResponseOk>(result)
        assertEquals("delete", result.data.name)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchDev(DbDevFilterRequest())
        assertIs<DbAdsResponseOk>(result)
        assertEquals("search", result.data.first().name)
    }

}
