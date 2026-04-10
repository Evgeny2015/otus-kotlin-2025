package ru.otus.otuskotlin.myproject.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransport
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = DevCreateRequest(
        dev = DevCreateDevice(
            name = "some dev",
            deviceType = DeviceType.DEVICE,
            roomId = "room id",
            deviceStatus = DeviceStatus.ONLINE,
            configuration = "{}",
            model = "model",
            visibility = DevVisibility.PUBLIC,
        ),
        debug = DevDebug(mode = DevRequestDebugMode.STUB, stub = DevRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IDevAppSettings = object : IDevAppSettings {
        override val corSettings: DevCorSettings = DevCorSettings()
        override val processor: DevProcessor = DevProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: DevCreateRequest): DevCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransport() as DevCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IDevAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<DevCreateRequest>()) },
            { toTransport() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as DevCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
