package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDevUpdateTest {
    abstract val repo: IRepoDev
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = DevId("repo-update-not-found")

    private val reqUpdateSucc by lazy {
        DevAd(
            id = updateSucc.id,
            name = "update object",
            deviceType = DevType.DEVICE,
            ownerId = DevUserId("owner-123"),
            visibility = DeviceVisibility.VISIBLE_TO_GROUP,
        )
    }
    private val reqUpdateNotFound = DevAd(
        id = updateIdNotFound,
        name = "update object not found",
        deviceType = DevType.DEVICE,
        ownerId = DevUserId("owner-123"),
        visibility = DeviceVisibility.VISIBLE_TO_GROUP,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateDev(DbDevRequest(reqUpdateSucc))
        assertIs<DbDevResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.name, result.data.name)
        assertEquals(reqUpdateSucc.deviceType, result.data.deviceType)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDev(DbDevRequest(reqUpdateNotFound))
        assertIs<DbDevResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDevs("update") {
        override val initObjects: List<DevAd> = listOf(
            createInitTestModel("update"),
        )
    }
}
