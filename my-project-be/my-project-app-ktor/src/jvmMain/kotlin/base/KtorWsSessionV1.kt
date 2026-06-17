package ru.otus.otuskotlin.myproject.app.ktor.base

import io.ktor.websocket.*
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.myproject.api.v1.models.IResponse
import ru.otus.otuskotlin.myproject.common.ws.IDevWsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : IDevWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
