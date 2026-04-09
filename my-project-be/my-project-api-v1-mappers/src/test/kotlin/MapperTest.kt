import org.junit.Test
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.common.*
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs
import ru.otus.otuskotlin.myproject.mappers.v1.*
import ru.otus.otuskotlin.myproject.stubs.*
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = DevCreateRequest(
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS,
            ),
            dev = DevStub.get().toTransportCreate()
        )
        val expected = DevStub.prepareResult {
            id = DevId.NONE
            ownerId = DevUserId.NONE
            lock = DevLock.NONE
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
            command = DevCommand.CREATE,
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

        val req = context.toTransport() as DevCreateResponse

        assertEquals(req.dev, DevStub.get().toTransport())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
