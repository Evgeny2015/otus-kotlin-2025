package ru.otus.otuskotlin.myproject.bl.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevAd(
            id = DevId("123-234-abc-ABC"),
            name = "abc",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_PUBLIC,
            lock = DevLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DevState.FAILING, ctx.state)
}

fun validationIdTrim(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevAd(
            id = DevId(" \n\t 123-234-abc-ABC \n\t "),
            name = "abc",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_PUBLIC,
            lock = DevLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DevState.FAILING, ctx.state)
}

fun validationIdEmpty(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevAd(
            id = DevId(""),
            name = "abc",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_PUBLIC,
            lock = DevLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DevState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevAd(
            id = DevId("!@#\$%^&*(),.{}"),
            name = "abc",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_PUBLIC,
            lock = DevLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DevState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
