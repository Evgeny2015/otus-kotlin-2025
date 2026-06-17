package ru.otus.otuskotlin.myproject.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.test.TestResult
import ru.otus.otuskotlin.myproject.api.v2.apiV2Mapper
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransportCreate
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransportDelete
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransportRead
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransportUpdate
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.module
import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevLock
import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V2DevRepoBaseTest {
    abstract val workMode: DevRequestDebugMode
    abstract val appSettingsCreate: DevAppSettings
    abstract val appSettingsRead:   DevAppSettings
    abstract val appSettingsUpdate: DevAppSettings
    abstract val appSettingsDelete: DevAppSettings
    abstract val appSettingsSearch: DevAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val uuidSup = "10000000-0000-0000-0000-000000000003"
    protected val initDev = DevStub.prepareResult {
        id = DevId(uuidOld)
        deviceType = DevType.DEVICE
        lock = DevLock(uuidOld)
    }
    protected val initDevSupply = DevStub.prepareResult {
        id = DevId(uuidSup)
        deviceType = DevType.DEVICE
    }


    @Test
    fun create() {
        val dev = initDev.toTransportCreate()
        v2TestApplication(
            conf = appSettingsCreate,
            func = "create",
            request = DevCreateRequest(
                dev = dev,
                debug = DevDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DevCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.dev?.id)
            assertEquals(dev.name, responseObj.dev?.name)
            assertEquals(dev.deviceType, responseObj.dev?.deviceType)
            assertEquals(dev.visibility, responseObj.dev?.visibility)
            assertEquals(uuidNew, responseObj.dev?.lock)
        }
    }

    @Test
    fun read() {
        val dev = initDev.toTransportRead()
        v2TestApplication(
            conf = appSettingsRead,
            func = "read",
            request = DevReadRequest(
                dev = dev,
                debug = DevDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DevReadResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.dev?.id)
        }
    }

    @Test
    fun update() {
        val dev = initDev.toTransportUpdate()
        v2TestApplication(
            conf = appSettingsUpdate,
            func = "update",
            request = DevUpdateRequest(
                dev = dev,
                debug = DevDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DevUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(dev.id, responseObj.dev?.id)
            assertEquals(dev.name, responseObj.dev?.name)
            assertEquals(dev.deviceType, responseObj.dev?.deviceType)
            assertEquals(dev.visibility, responseObj.dev?.visibility)
        }
    }
    @Test
    fun delete() {
        val dev = initDev.toTransportDelete()
        v2TestApplication(
            conf = appSettingsDelete,
            func = "delete",
            request = DevDeleteRequest(
                dev = dev,
                debug = DevDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<DevDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.dev?.id)
        }
    }

    @Test
    fun search() = v2TestApplication(
        conf = appSettingsSearch,
        func = "search",
        request = DevSearchRequest(
            devFilter = DevSearchFilter(),
            debug = DevDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<DevSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.devs?.size)
        assertEquals(uuidOld, responseObj.devs?.first()?.id)
    }

    private inline fun <reified T: IRequest> v2TestApplication(
        conf: DevAppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): TestResult = testApplication {
        application { module(appSettings = conf) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        val response = client.post("/v2/dev/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
