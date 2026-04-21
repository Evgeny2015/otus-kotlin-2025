package ru.otus.otuskotlin.myproject.bl.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevFilter
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(DevState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(DevState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(DevState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(DevState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(DevState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
