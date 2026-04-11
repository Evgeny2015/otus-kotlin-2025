package ru.otus.otuskotlin.myproject.app.ktor.stub

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.myproject.api.v2.apiV2Mapper
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.module
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals

class V2DevStubApiTest {

    @Test
    fun create() = v2TestApplication(
        func = "create",
        request = DevCreateRequest(
            dev = DevCreateDevice(
                name = "device 01",
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
    fun read() = v2TestApplication(
        func = "read",
        request = DevReadRequest(
            dev = DevReadDevice("01"),
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
    fun update() = v2TestApplication(
        func = "update",
        request = DevUpdateRequest(
            dev = DevUpdateDevice(
                id = "001",
                name = "device 01",
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
    fun delete() = v2TestApplication(
        func = "delete",
        request = DevDeleteRequest(
            dev = DevDeleteDevice(
                id = "01",
                lock = "123"
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
    fun search() = v2TestApplication(
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

    private inline fun <reified T: IRequest> v2TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(DevAppSettings(corSettings = DevCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/dev/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
