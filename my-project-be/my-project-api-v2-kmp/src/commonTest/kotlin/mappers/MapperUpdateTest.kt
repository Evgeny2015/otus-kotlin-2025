package ru.otus.otuskotlin.myproject.api.v2.mappers

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.api.v2.mappers.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = DevUpdateRequest(
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS,
            ),
            dev = DevStub.get().toTransportUpdate(),
        )
        val expected = DevStub.prepareResult {
            ownerId = DevUserId.NONE
            permissionsClient.clear()
        }

        val context = DevContext()
        context.fromTransport(req)

        assertEquals(DevStubs.SUCCESS, context.stubCase)
        assertEquals(DevWorkMode.STUB, context.workMode)
        assertEquals(expected, context.devRequest)
    }

    @Test
    fun toTransport() {
        val context = DevContext(
            requestId = DevRequestId("1234"),
            command = DevCommand.UPDATE,
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

        val req = context.toTransport() as DevUpdateResponse

        assertEquals(DevStub.get().toTransport(), req.dev)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
