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

class BlRepoReadTest {

    private val userId = DevUserId("321")
    private val command = DevCommand.READ
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
        }
    )
    private val settings = DevCorSettings(repoTest = repo)
    private val processor = DevProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devRequest = DevAd(
                id = DevId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(DevState.FINISHING, ctx.state)
        assertEquals(initDev.id, ctx.devResponse.id)
        assertEquals(initDev.name, ctx.devResponse.name)
        assertEquals(initDev.deviceType, ctx.devResponse.deviceType)
        assertEquals(initDev.visibility, ctx.devResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
