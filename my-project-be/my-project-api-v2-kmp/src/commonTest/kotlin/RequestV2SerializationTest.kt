package ru.otus.otuskotlin.myproject.api.v2

import ru.otus.otuskotlin.myproject.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = DevCreateRequest(
        debug = DevDebug(
            mode = DevRequestDebugMode.STUB,
            stub = DevRequestDebugStubs.BAD_NAME
        ),
        dev = DevCreateDevice(
            name = "title",
            deviceType = DeviceType.DEVICE,
            roomId = "019d49ed-9102-7cc1-9a5f-532bf2b6d863",
            deviceStatus = DeviceStatus.ONLINE,
            configuration = "{}",
            lastSeen = "01-01-2000",
            manufacturer = "manufacturer",
            model = "model"        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"name\":\\s*\"title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as DevCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"dev": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<DevCreateRequest>(jsonString)

        assertEquals(null, obj.dev)
    }
}
