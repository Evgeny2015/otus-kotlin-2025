package ru.otus.otuskotlin.myproject.bl.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevFilter
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devValidating = DevAd(name = ""))
        chain.exec(ctx)
        assertEquals(DevState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devValidating = DevAd(name = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(DevState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = DevContext(state = DevState.RUNNING, devFilterValidating = DevFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(DevState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateNameHasContent("")
        }.build()
    }
}
