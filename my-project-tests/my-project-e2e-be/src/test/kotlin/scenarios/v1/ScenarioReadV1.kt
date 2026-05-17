package ru.otus.otuskotlin.project.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.someCreateDev
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadV1(
    private val client: Client,
    private val debug: DevDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateDev
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

        val rObj = DevReadDevice(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "dev/read",
            DevReadRequest(
                requestType = "read",
                debug = debug,
                dev = rObj,
            )
        ) as DevReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: DevResponseDevice = resRead.dev ?: fail("No dev in Read response")
        assertEquals(obj.name, rrObj.name)
        assertEquals(obj.visibility, rrObj.visibility)
        assertEquals(obj.deviceType, rrObj.deviceType)

        val resDelete = client.sendAndReceive(
            "dev/delete", DevDeleteRequest(
                requestType = "delete",
                debug = debug,
                dev = DevDeleteDevice(cObj.id, cObj.lock),
            )
        ) as DevDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: DevResponseDevice = resDelete.dev ?: fail("No dev in Delete response")
        assertEquals(obj.name, dObj.name)
        assertEquals(obj.visibility, dObj.visibility)
        assertEquals(obj.deviceType, dObj.deviceType)
    }
}