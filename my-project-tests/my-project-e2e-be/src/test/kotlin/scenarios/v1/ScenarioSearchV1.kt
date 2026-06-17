package ru.otus.otuskotlin.project.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.someCreateDev
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
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
            val resCreate = client.sendAndReceive(
                "dev/create", DevCreateRequest(
                    requestType = "create",
                    debug = debug,
                    dev = obj,
                )
            ) as DevCreateResponse

            assertEquals(ResponseResult.SUCCESS, resCreate.result)

            val cObj: DevResponseDevice = resCreate.dev ?: fail("No dev in Create response")
            assertEquals(obj.name, cObj.name)
            assertEquals(obj.visibility, cObj.visibility)
            assertEquals(obj.deviceType, cObj.deviceType)
            cObj
        }

        val sObj = DevSearchFilter(searchString = "device")
        val resSearch = client.sendAndReceive(
            "dev/search",
            DevSearchRequest(
                requestType = "search",
                debug = debug,
                devFilter = sObj,
            )
        ) as DevSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<DevResponseDevice> = resSearch.devs ?: fail("No devs in Search response")
        val names = rsObj.map { it.name }
        assertContains(names, "Device 01")
        assertContains(names, "Device 02")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "dev/delete", DevDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    dev = DevDeleteDevice(obj.id, obj.lock),
                )
            ) as DevDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}