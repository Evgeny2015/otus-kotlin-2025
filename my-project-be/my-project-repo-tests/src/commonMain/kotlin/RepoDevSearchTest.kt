package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.models.DevUserId
import ru.otus.otuskotlin.myproject.common.repo.DbDevFilterRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevsResponseOk
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDevSearchTest {
    abstract val repo: IRepoDev

    protected open val initializedObjects: List<DevAd> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchDev(DbDevFilterRequest(ownerId = searchOwnerId))
        assertIs<DbDevsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchDevice() = runRepoTest {
        val result = repo.searchDev(DbDevFilterRequest(devType = DevType.DEVICE))
        assertIs<DbDevsResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitDevs("search") {

        val searchOwnerId = DevUserId("owner-124")
        override val initObjects: List<DevAd> = listOf(
            createInitTestModel("dev1"),
            createInitTestModel("dev2", ownerId = searchOwnerId),
            createInitTestModel("dev3", devType = DevType.DEVICE),
            createInitTestModel("dev4", ownerId = searchOwnerId),
            createInitTestModel("dev5", devType = DevType.DEVICE),
        )
    }
}
