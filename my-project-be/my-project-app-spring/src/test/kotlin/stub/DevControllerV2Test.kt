package ru.otus.otuskotlin.myproject.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.myproject.app.spring.config.DevConfig
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV2Fine
import ru.otus.otuskotlin.myproject.api.v2.mappers.*
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(DevControllerV2Fine::class, DevConfig::class)
internal class DevControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockitoBean
    private lateinit var processor: DevProcessor

    @Test
    fun createDev() = testStubDev(
        "/v2/dev/create",
        DevCreateRequest(),
        DevContext().toTransportCreate()
    )

    @Test
    fun readDev() = testStubDev(
        "/v2/dev/read",
        DevReadRequest(),
        DevContext().toTransportRead()
    )

    @Test
    fun updateDev() = testStubDev(
        "/v2/dev/update",
        DevUpdateRequest(),
        DevContext().toTransportUpdate()
    )

    @Test
    fun deleteDev() = testStubDev(
        "/v2/dev/delete",
        DevDeleteRequest(),
        DevContext().toTransportDelete()
    )

    @Test
    fun searchDev() = testStubDev(
        "/v2/dev/search",
        DevSearchRequest(),
        DevContext().toTransportSearch()
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
