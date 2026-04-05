package ru.otus.otuskotlin.myproject.api.v1

import ru.otus.otuskotlin.myproject.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = DevCreateResponse(
        dev = DevResponseDevice(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"name\":\\s*\"title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DevCreateResponse

        assertEquals(response, obj)
    }
}
