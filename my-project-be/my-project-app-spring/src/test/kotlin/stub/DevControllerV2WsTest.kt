package ru.otus.otuskotlin.myproject.app.spring.stub

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.myproject.api.v2.apiV2ResponseDeserialize
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class DevControllerV2WsTest: DevControllerBaseWsTest<IRequest, IResponse>("v2") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV2ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV2RequestSerialize(request)

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
}
