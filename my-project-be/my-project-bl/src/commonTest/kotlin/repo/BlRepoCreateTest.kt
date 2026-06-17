package ru.otus.otuskotlin.myproject.bl.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.backend.repo.tests.DevRepositoryMock
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlRepoCreateTest {

    private val userId = DevUserId("321")
    private val command = DevCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = DevRepositoryMock(
        invokeCreateDev = {
            DbDevResponseOk(
                data = DevAd(
                    id = DevId(uuid),
                    name = it.dev.name,
                    deviceType = it.dev.deviceType,
                    ownerId = userId,
                    visibility = it.dev.visibility,
                )
            )
        }
    )
    private val settings = DevCorSettings(
        repoTest = repo
    )
    private val processor = DevProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devRequest = DevAd(
                name = "abc",
                deviceType = DevType.DEVICE,
                visibility = DeviceVisibility.VISIBLE_PUBLIC,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevState.FINISHING, ctx.state)
        assertNotEquals(DevId.NONE, ctx.devResponse.id)
        assertEquals("abc", ctx.devResponse.name)
        assertEquals(DevType.DEVICE, ctx.devResponse.deviceType)
        assertEquals(DeviceVisibility.VISIBLE_PUBLIC, ctx.devResponse.visibility)
    }
}
