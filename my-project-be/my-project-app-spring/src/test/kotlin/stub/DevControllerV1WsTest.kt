package ru.otus.otuskotlin.myproject.app.spring.stub

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.myproject.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.myproject.api.v1.models.*
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class DevControllerV1WsTest: DevControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(DevCreateRequest(
        debug = DevDebug(DevRequestDebugMode.STUB, DevRequestDebugStubs.SUCCESS),
        dev = DevCreateDevice(
            name = "test1",
            deviceType = DeviceType.DEVICE,
            visibility = DevVisibility.PUBLIC,
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is DevInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is DevCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }

    @Test
    fun wsRead(): Unit = testWsApp(DevReadRequest(
        debug = DevDebug(DevRequestDebugMode.STUB, DevRequestDebugStubs.SUCCESS),
        dev = DevReadDevice(
            id = "01"
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesRead = pl[1]
        assert(mesInit is DevInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesRead is DevReadResponse)
        assert(mesRead.result == ResponseResult.SUCCESS)
    }

    @Test
    fun wsUpdate(): Unit = testWsApp(DevUpdateRequest(
        debug = DevDebug(DevRequestDebugMode.STUB, DevRequestDebugStubs.SUCCESS),
        dev = DevUpdateDevice(
            id = "01",
            name = "xx",
            deviceType = DeviceType.DEVICE,
            visibility = DevVisibility.OWNER_ONLY,
            lock = "123",
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesUpdate = pl[1]
        assert(mesInit is DevInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesUpdate is DevUpdateResponse)
        assert(mesUpdate.result == ResponseResult.SUCCESS)
    }
}
