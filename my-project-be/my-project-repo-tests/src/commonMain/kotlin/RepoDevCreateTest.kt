package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.repo.common.IRepoDevInitializable
import kotlin.test.*


abstract class RepoDevCreateTest {
    abstract val repo: IRepoDevInitializable
    protected open val uuidNew = DevId("10000000-0000-0000-0000-000000000001")

    private val createObj = DevAd(
        name = "create object",
        deviceType = DevType.DEVICE,
        ownerId = DevUserId("owner-123"),
        visibility = DeviceVisibility.VISIBLE_TO_GROUP,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createDev(DbDevRequest(createObj))
        val expected = createObj
        assertIs<DbDevResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.name, result.data.name)
        assertEquals(expected.deviceType, result.data.deviceType)
        assertNotEquals(DevId.NONE, result.data.id)
    }

    companion object : BaseInitDevs("create") {
        override val initObjects: List<DevAd> = emptyList()
    }
}
