package ru.otus.otuskotlin.myproject.bl.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DevStub.get()

fun validationDeviceTypeCorrect(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DevState.FAILING, ctx.state)
}

fun validationDeviceTypeInCorrect(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevStub.prepareResult {
            deviceType = DevType.NONE
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DevState.FAILING, ctx.state)
}