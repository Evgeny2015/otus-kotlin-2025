package ru.otus.otuskotlin.myproject.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

import ru.otus.otuskotlin.myproject.app.spring.config.DevConfig
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV1Fine
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.mappers.v1.*
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import org.junit.jupiter.api.Test
import org.springframework.test.context.bean.override.mockito.MockitoBean


@WebFluxTest(DevControllerV1Fine::class, DevConfig::class)
internal class DevControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: DevProcessor

    @Test
    fun createDev() = testStubDev(
        "/v1/dev/create",
        DevCreateRequest(),
        DevContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readDev() = testStubDev(
        "/v1/dev/read",
        DevReadRequest(),
        DevContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateDev() = testStubDev(
        "/v1/dev/update",
        DevUpdateRequest(),
        DevContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteDev() = testStubDev(
        "/v1/dev/delete",
        DevDeleteRequest(),
        DevContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchDev() = testStubDev(
        "/v1/dev/search",
        DevSearchRequest(),
        DevContext().toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubDev(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        println("EXPECTED RESPONSE: $responseObj")

        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("ACTUAL RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
