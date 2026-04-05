package ru.otus.otuskotlin.myproject.api.v2

import ru.otus.otuskotlin.myproject.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = DevCreateResponse(
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
//        val json = apiV2Mapper.encodeToString(AdRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"name\":\\s*\"title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as DevCreateResponse

        assertEquals(response, obj)
    }
}
