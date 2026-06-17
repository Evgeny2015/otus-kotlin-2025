package ru.otus.otuskotlin.myproject.app.spring.base

import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.myproject.api.v1.models.IResponse
import ru.otus.otuskotlin.myproject.common.ws.IDevWsSession

data class SpringWsSessionV1(
    private val session: WebSocketSession,
) : IDevWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV1ResponseSerialize(obj)
        println("SENDING to WsV1: $message")
        session.send(Mono.just(session.textMessage(message)))
    }
}
