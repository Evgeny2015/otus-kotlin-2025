package ru.otus.otuskotlin.myproject.bl.repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.backend.repo.tests.DevRepositoryMock
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BlRepoSearchTest {

    private val userId = DevUserId("321")
    private val command = DevCommand.SEARCH
    private val initAd = DevAd(
        id = DevId("123"),
        name = "abc",
        deviceType = DevType.DEVICE,
        ownerId = userId,
        visibility = DeviceVisibility.VISIBLE_PUBLIC,
    )
    private val repo = DevRepositoryMock(
        invokeSearchDev = {
            DbDevsResponseOk(
                data = listOf(initAd),
            )
        }
    )
    private val settings = DevCorSettings(repoTest = repo)
    private val processor = DevProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devFilterRequest = DevFilter(
                searchString = "abc",
                devType = DevType.DEVICE
            ),
        )
        processor.exec(ctx)
        assertEquals(DevState.FINISHING, ctx.state)
        assertEquals(1, ctx.devsResponse.size)
    }
}
