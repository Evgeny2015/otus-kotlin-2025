package validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: DevCommand, processor: DevProcessor) = runTest {
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

fun validationLockTrim(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevStub.prepareResult {
            lock = DevLock(" \n\t 123 \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DevState.FAILING, ctx.state)
}

fun validationLockEmpty(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevStub.prepareResult {
            lock = DevLock("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DevState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: DevCommand, processor: DevProcessor) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevStub.prepareResult {
            lock = DevLock("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DevState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
