package ru.otus.otuskotlin.myproject.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

import ru.otus.otuskotlin.myproject.app.spring.config.DevConfig
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV1Fine
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.mappers.v1.*
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.stubs.DevStub
import org.junit.jupiter.api.Test


@WebFluxTest(DevControllerV1Fine::class, DevConfig::class)
internal class DevControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    private fun stubContext() = DevContext().apply {
        state = DevState.RUNNING
        devResponse = DevStub.get()
    }

    @Test
    fun createAd() = testStubDev(
        "/v1/dev/create",
        DevCreateRequest(),
        stubContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readAd() = testStubDev(
        "/v1/dev/read",
        DevReadRequest(),
        stubContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateAd() = testStubDev(
        "/v1/dev/update",
        DevUpdateRequest(),
        stubContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteAd() = testStubDev(
        "/v1/dev/delete",
        DevDeleteRequest(),
        stubContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchAd() = testStubDev(
        "/v1/dev/search",
        DevSearchRequest(),
        stubContext().toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubDev(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
