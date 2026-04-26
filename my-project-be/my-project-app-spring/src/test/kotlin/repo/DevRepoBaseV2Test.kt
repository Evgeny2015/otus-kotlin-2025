package ru.otus.otuskotlin.myproject.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.myproject.api.v2.mappers.*
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test

internal abstract class DevRepoBaseV2Test {
    protected abstract var webClient: WebTestClient
    private val debug = DevDebug(mode = DevRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createDev() = testRepoDev(
        "create",
        DevCreateRequest(
            dev = DevStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(DevStub.prepareResult {
            id = DevId(uuidNew)
            ownerId = DevUserId.NONE
            lock = DevLock.NONE
        })
            .toTransportCreate()
    )

    @Test
    open fun readDev() = testRepoDev(
        "read",
        DevReadRequest(
            dev = DevStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(DevStub.get())
            .toTransportRead()
    )

    @Test
    open fun updateDev() = testRepoDev(
        "update",
        DevUpdateRequest(
            dev = DevStub.prepareResult { name = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(DevStub.prepareResult { name = "add" })
            .toTransportUpdate()
    )

    @Test
    open fun deleteDev() = testRepoDev(
        "delete",
        DevDeleteRequest(
            dev = DevStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(DevStub.get())
            .toTransportDelete()
    )

    @Test
    open fun searchDev() = testRepoDev(
        "search",
        DevSearchRequest(
            devFilter = DevSearchFilter(devType = DeviceType.SENSOR),
            debug = debug,
        ),
        DevContext(
            state = DevState.RUNNING,
            devsResponse = DevStub.prepareSearchList("xx", DevType.SENSOR)
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch()
    )

    private fun prepareCtx(dev: DevAd) = DevContext(
        state = DevState.RUNNING,
        devResponse = dev.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoDev(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v2/dev/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is DevSearchResponse -> it.copy(devs = it.devs?.sortedBy { it.id })
                    null -> throw RuntimeException("Null response")
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
