package ru.otus.otuskotlin.myproject.bl.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import ru.otus.otuskotlin.myproject.backend.repo.tests.DevRepositoryMock
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlRepoDeleteTest {

    private val userId = DevUserId("321")
    private val command = DevCommand.DELETE
    private val initAd = DevAd(
        id = DevId("123"),
        name = "abc",
        deviceType = DevType.DEVICE,
        ownerId = userId,
        visibility = DeviceVisibility.VISIBLE_PUBLIC,
        lock = DevLock("123-lock"),
    )
    private val repo = DevRepositoryMock(
        invokeReadDev = {
            DbDevResponseOk(
                data = initAd,
            )
        },
        invokeDeleteDev = {
            if (it.id == initAd.id)
                DbDevResponseOk(
                    data = initAd
                )
            else DbDevResponseErr()
        }
    )
    private val settings by lazy {
        DevCorSettings(
            repoTest = repo
        )
    }
    private val processor = DevProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val devToUpdate = DevAd(
            id = DevId("123"),
            lock = DevLock("123-lock"),
        )
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devRequest = devToUpdate,
        )
        processor.exec(ctx)
        assertEquals(DevState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.devResponse.id)
        assertEquals(initAd.name, ctx.devResponse.name)
        assertEquals(initAd.deviceType, ctx.devResponse.deviceType)
        assertEquals(initAd.visibility, ctx.devResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
