package ru.otus.otuskotlin.myproject.bl.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DevSearchStubTest {

    private val processor = DevProcessor()
    val filter = DevFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = DevContext(
            command = DevCommand.SEARCH,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.SUCCESS,
            devFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.devsResponse.size > 1)
        val first = ctx.devsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.name.contains(filter.searchString))
        with (DevStub.get()) {
            assertEquals(deviceType, first.deviceType)
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = DevContext(
            command = DevCommand.SEARCH,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_ID,
            devFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DevContext(
            command = DevCommand.SEARCH,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.DB_ERROR,
            devFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = DevContext(
            command = DevCommand.SEARCH,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_NAME,
            devFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
