package ru.otus.otuskotlin.myproject.bl.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevFilter
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.models.DevWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BlValidationSearchTest: BaseBlValidationTest() {
    override val command = DevCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = DevContext(
            command = command,
            state = DevState.NONE,
            workMode = DevWorkMode.TEST,
            devFilterRequest = DevFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(DevState.FAILING, ctx.state)
    }
}
