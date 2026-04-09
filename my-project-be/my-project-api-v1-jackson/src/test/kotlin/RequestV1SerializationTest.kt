package ru.otus.otuskotlin.myproject.api.v1

import ru.otus.otuskotlin.myproject.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = DevCreateRequest(
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
            model = "model"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"name\":\\s*\"title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DevCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"dev": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, DevCreateRequest::class.java)

        assertEquals(null, obj.dev)
    }
}
