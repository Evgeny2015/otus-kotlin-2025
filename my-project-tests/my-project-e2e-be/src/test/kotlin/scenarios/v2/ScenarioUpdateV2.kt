package ru.otus.otuskotlin.project.e2e.be.scenarios.v2

import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.scenarios.v2.base.sendAndReceive
import ru.otus.otuskotlin.project.e2e.be.scenarios.v2.base.someCreateDev
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV2(
    private val client: Client,
    private val debug: DevDebug? = null
) {
    @Test
    fun update() = runBlocking {
        val obj = someCreateDev
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

        val uObj = DevUpdateDevice(
            id = cObj.id,
            lock = cObj.lock,
            name = "Device 02",
            deviceType = cObj.deviceType,
            visibility = cObj.visibility,
        )
        val resUpdate = client.sendAndReceive<DevUpdateRequest,DevUpdateResponse>(
            "dev/update",
            DevUpdateRequest(
                debug = debug,
                dev = uObj,
            )
        )

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: DevResponseDevice = resUpdate.dev ?: fail("No dev in Update response")
        assertEquals(uObj.name, ruObj.name)
        assertEquals(uObj.visibility, ruObj.visibility)
        assertEquals(uObj.deviceType, ruObj.deviceType)

        val resDelete = client.sendAndReceive<DevDeleteRequest,DevDeleteResponse>(
            "dev/delete", DevDeleteRequest(
                debug = debug,
                dev = DevDeleteDevice(cObj.id, resUpdate.dev?.lock),
            )
        )

        assertEquals(ResponseResult.SUCCESS, resDelete.result)
    }
}