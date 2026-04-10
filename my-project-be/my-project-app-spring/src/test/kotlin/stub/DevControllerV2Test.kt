package ru.otus.otuskotlin.myproject.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.myproject.app.spring.config.DevConfig
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV2Fine
import ru.otus.otuskotlin.myproject.api.v2.mappers.*
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(DevControllerV2Fine::class, DevConfig::class)
internal class DevControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    private fun stubContext() = DevContext().apply {
        state = DevState.RUNNING
        devResponse = DevStub.get()
    }

    @Suppress("unused")
    private lateinit var processor: DevProcessor

    @Test
    fun createAd() = testStubAd(
        "/v2/ad/create",
        DevCreateRequest(),
        stubContext().toTransportCreate()
    )

    @Test
    fun readAd() = testStubAd(
        "/v2/ad/read",
        DevReadRequest(),
        stubContext().toTransportRead()
    )

    @Test
    fun updateAd() = testStubAd(
        "/v2/ad/update",
        DevUpdateRequest(),
        stubContext().toTransportUpdate()
    )

    @Test
    fun deleteAd() = testStubAd(
        "/v2/ad/delete",
        DevDeleteRequest(),
        stubContext().toTransportDelete()
    )

    @Test
    fun searchAd() = testStubAd(
        "/v2/ad/search",
        DevSearchRequest(),
        stubContext().toTransportSearch()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
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
