package ru.otus.otuskotlin.myproject.app.spring.controllers

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import ru.otus.otuskotlin.myproject.app.spring.base.DevAppSettings
import ru.otus.otuskotlin.myproject.app.spring.base.SpringWsSessionV2
import ru.otus.otuskotlin.myproject.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.myproject.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.myproject.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransport
import ru.otus.otuskotlin.myproject.api.v2.models.IRequest
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevCommand

@Component
class DevControllerV2Ws(private val appSettings: DevAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> {
        val mkplSess = SpringWsSessionV2(session)
        sessions.add(mkplSess)
        val messageObj = flow {
            emit(process("ws-v2-init") {
                command = DevCommand.INIT
                wsSession = mkplSess
            })
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v2-handle") {
                    wsSession = mkplSess
                    val request = apiV2RequestDeserialize<IRequest>(message.payloadAsText)
                    fromTransport(request)
                }
            }

        val output = merge(messageObj, messages)
            .onCompletion {
                process("ws-v2-finish") {
                    command = DevCommand.FINISH
                    wsSession = mkplSess
                }
                sessions.remove(mkplSess)
            }
            .map { session.textMessage(apiV2ResponseSerialize(it)) }
            .asFlux()
        return session.send(output)
    }

    private suspend fun process(logId: String, function: DevContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = DevContext::toTransport,
        clazz = this@DevControllerV2Ws::class,
        logId = logId,
    )
}
