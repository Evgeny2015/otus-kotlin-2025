package ru.otus.otuskotlin.myproject.bl.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals

class DevDeleteStubTest {

    private val processor = DevProcessor()
    val id = DevId("01")

    @Test
    fun delete() = runTest {

        val ctx = DevContext(
            command = DevCommand.DELETE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.SUCCESS,
            devRequest = DevAd(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = DevStub.get()
        assertEquals(stub.id, ctx.devResponse.id)
        assertEquals(stub.name, ctx.devResponse.name)
        assertEquals(stub.deviceType, ctx.devResponse.deviceType)
        assertEquals(stub.visibility, ctx.devResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = DevContext(
            command = DevCommand.DELETE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_ID,
            devRequest = DevAd(),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DevContext(
            command = DevCommand.DELETE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.DB_ERROR,
            devRequest = DevAd(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = DevContext(
            command = DevCommand.DELETE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_NAME,
            devRequest = DevAd(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
