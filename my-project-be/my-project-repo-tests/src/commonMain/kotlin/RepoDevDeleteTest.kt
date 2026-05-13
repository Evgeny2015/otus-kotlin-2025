package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoDevDeleteTest {
    abstract val repo: IRepoDev
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = DevId("dev-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteDev(DbDevIdRequest(deleteSucc.id, lock = lockOld))

        assertIs<DbDevResponseOk>(result)
        assertEquals(deleteSucc.name, result.data.name)
        assertEquals(deleteSucc.deviceType, result.data.deviceType)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readDev(DbDevIdRequest(notFoundId, lock = lockOld))

        assertIs<DbDevResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteDev(DbDevIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbDevResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitDevs("delete") {
        override val initObjects: List<DevAd> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
