package ru.otus.otuskotlin.project.e2e.be.scenarios.v1

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.sendAndReceive
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.base.someCreateDev
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateDeleteV1(
    private val client: Client,
    private val debug: DevDebug? = null
) {
    @Test
    fun createDelete() = runBlocking {
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