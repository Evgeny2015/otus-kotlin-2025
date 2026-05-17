package ru.otus.otuskotlin.project.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.scenarios.v2.base.sendAndReceive
import ru.otus.otuskotlin.project.e2e.be.scenarios.v2.base.someCreateDev
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV2(
    private val client: Client,
    private val debug: DevDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateDev,
            someCreateDev.copy(name = "Device 01"),
            someCreateDev.copy(name = "Device 02"),
        ).map { obj ->
            val resCreate = client.sendAndReceive<DevCreateRequest,DevCreateResponse>(
                "dev/create", DevCreateRequest(
                    debug = debug,
                    dev = obj,
                )
            )

            assertEquals(ResponseResult.SUCCESS, resCreate.result)

            val cObj: DevResponseDevice = resCreate.dev ?: fail("No dev in Create response")
            assertEquals(obj.name, cObj.name)
            assertEquals(obj.visibility, cObj.visibility)
            assertEquals(obj.deviceType, cObj.deviceType)
            cObj
        }

        val sObj = DevSearchFilter(searchString = "device")
        val resSearch = client.sendAndReceive<DevSearchRequest,DevSearchResponse>(
            "dev/search",
            DevSearchRequest(
                debug = debug,
                devFilter = sObj,
            )
        )

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<DevResponseDevice> = resSearch.devs ?: fail("No devs in Search response")
        val titles = rsObj.map { it.name }
        assertContains(titles, "Device 01")
        assertContains(titles, "Device 02")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive<DevDeleteRequest,DevDeleteResponse>(
                "dev/delete", DevDeleteRequest(
                    debug = debug,
                    dev = DevDeleteDevice(obj.id, obj.lock),
                )
            )

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}