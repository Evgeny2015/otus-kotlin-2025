package ru.otus.otuskotlin.myproject.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV1Ws
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV2Ws


@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val devControllerV1: DevControllerV1Ws,
    private val devControllerV2: DevControllerV2Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to devControllerV1,
            "/v2/ws" to devControllerV2,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}
