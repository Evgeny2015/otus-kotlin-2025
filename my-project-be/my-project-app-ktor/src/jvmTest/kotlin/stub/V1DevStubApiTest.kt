package ru.otus.otuskotlin.myproject.app.ktor.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.moduleJvm
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.otuskotlin.myproject.stubs.DevStub

class V1DevStubApiTest {
    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = DevCreateRequest(
            dev = DevCreateDevice(
                name = "device 01",
                deviceType = DeviceType.DEVICE,
                visibility = DevVisibility.PUBLIC,
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DevCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(DevStub.get().id.asString(), responseObj.dev?.id)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = DevReadRequest(
            dev = DevReadDevice("00"),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DevReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(DevStub.get().id.asString(), responseObj.dev?.id)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = DevUpdateRequest(
            dev = DevUpdateDevice(
                id = "01",
                name = "device 01",
                deviceType = DeviceType.DEVICE,
                visibility = DevVisibility.PUBLIC,
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DevUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(DevStub.get().id.asString(), responseObj.dev?.id)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = DevDeleteRequest(
            dev = DevDeleteDevice(
                id = "01",
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DevDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(DevStub.get().id.asString(), responseObj.dev?.id)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = DevSearchRequest(
            adFilter = DevSearchFilter(),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<DevSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals(null, responseObj.ads?.first()?.id)
    }

    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(DevAppSettings(corSettings = DevCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/dev/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
