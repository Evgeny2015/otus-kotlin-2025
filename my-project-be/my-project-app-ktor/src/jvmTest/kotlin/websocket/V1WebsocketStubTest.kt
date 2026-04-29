package ru.otus.otuskotlin.myproject.app.ktor.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.moduleJvm
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = DevCreateRequest(
            dev = DevCreateDevice(
                name = "device",
                deviceType = DeviceType.DEVICE,
                visibility = DevVisibility.PUBLIC,
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = DevReadRequest(
            dev = DevReadDevice("01"),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = DevUpdateRequest(
            dev = DevUpdateDevice(
                id = "01",
                name = "Болт",
                deviceType = DeviceType.DEVICE,
                visibility = DevVisibility.PUBLIC,
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = DevDeleteRequest(
            dev = DevDeleteDevice(
                id = "01",
            ),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = DevSearchRequest(
            devFilter = DevSearchFilter(),
            debug = DevDebug(
                mode = DevRequestDebugMode.STUB,
                stub = DevRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { moduleJvm(DevAppSettings(corSettings = DevCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<DevInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}
