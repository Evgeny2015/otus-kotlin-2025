package ru.otus.otuskotlin.myproject.api.v2.mappers

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.mappers.v2.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val ad = DevStub.get()
        val req = DevDeleteRequest(
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS,
            ),
            dev = DevStub.get().toTransportDelete(),
        )

        val context = DevContext()
        context.fromTransport(req)

        assertEquals(DevStubs.SUCCESS, context.stubCase)
        assertEquals(DevWorkMode.STUB, context.workMode)
        assertEquals(ad.id.toTransport(), context.devRequest.id.asString())
        assertEquals(ad.lock.toTransport(), context.devRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = DevContext(
            requestId = DevRequestId("1234"),
            command = DevCommand.DELETE,
            devResponse = DevStub.get(),
            errors = mutableListOf(
                DevError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = DevState.RUNNING,
        )

        val req = context.toTransport() as DevDeleteResponse

        assertEquals(DevStub.get().toTransport(), req.dev)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
