package ru.otus.otuskotlin.myproject.bl.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals

class DevCreateStubTest {

    private val processor = DevProcessor()
    val id = DevStub.get().id
    val name = "name 01"
    val deviceType = DevType.DEVICE
    val visibility = DeviceVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = DevContext(
            command = DevCommand.CREATE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.SUCCESS,
            devRequest = DevAd(
                id = id,
                name = name,
                deviceType = deviceType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.devResponse.id)
        assertEquals(name, ctx.devResponse.name)
        assertEquals(deviceType, ctx.devResponse.deviceType)
        assertEquals(visibility, ctx.devResponse.visibility)
    }

    @Test
    fun badName() = runTest {
        val ctx = DevContext(
            command = DevCommand.CREATE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_NAME,
            devRequest = DevAd(
                id = id,
                name = "",
                deviceType = deviceType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDeviceType() = runTest {
        val ctx = DevContext(
            command = DevCommand.CREATE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_DEVICE_TYPE,
            devRequest = DevAd(
                id = id,
                name = name,
                deviceType = deviceType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("deviceType", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = DevContext(
            command = DevCommand.CREATE,
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
            command = DevCommand.CREATE,
            state = DevState.NONE,
            workMode = DevWorkMode.STUB,
            stubCase = DevStubs.BAD_ID,
            devRequest = DevAd(
                id = id,
                name = name,
                deviceType = deviceType,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(DevAd(), ctx.devResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
