package ru.otus.otuskotlin.myproject.bl.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.myproject.backend.repo.tests.DevRepositoryMock
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BlRepoUpdateTest {

    private val userId = DevUserId("321")
    private val command = DevCommand.UPDATE
    private val initDev = DevAd(
        id = DevId("123"),
        name = "abc",
        deviceType = DevType.DEVICE,
        ownerId = userId,
        visibility = DeviceVisibility.VISIBLE_PUBLIC,
    )
    private val repo = DevRepositoryMock(
        invokeReadDev = {
            DbDevResponseOk(
                data = initDev,
            )
        },
        invokeUpdateDev = {
            DbDevResponseOk(
                data = DevAd(
                    id = DevId("123"),
                    name = "xyz",
                    deviceType = DevType.DEVICE,
                    visibility = DeviceVisibility.VISIBLE_TO_GROUP,
                )
            )
        }
    )
    private val settings = DevCorSettings(repoTest = repo)
    private val processor = DevProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = DevAd(
            id = DevId("123"),
            name = "xyz",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_TO_GROUP,
            lock = DevLock("123"),
        )
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(DevState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.devResponse.id)
        assertEquals(adToUpdate.name, ctx.devResponse.name)
        assertEquals(adToUpdate.deviceType, ctx.devResponse.deviceType)
        assertEquals(adToUpdate.visibility, ctx.devResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
