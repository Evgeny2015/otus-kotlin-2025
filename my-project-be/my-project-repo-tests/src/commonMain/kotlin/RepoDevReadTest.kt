package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.repo.DbDevIdRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDevReadTest {
    abstract val repo: IRepoDev
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readDev(DbDevIdRequest(readSucc.id))

        assertIs<DbDevResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readDev(DbDevIdRequest(notFoundId))

        assertIs<DbDevResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDevs("delete") {
        override val initObjects: List<DevAd> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = DevId("ad-repo-read-notFound")
    }
}
