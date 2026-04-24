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
import ru.otus.otuskotlin.myproject.app.spring.base.SpringWsSessionV1
import ru.otus.otuskotlin.myproject.api.v1.apiV1Mapper
import ru.otus.otuskotlin.myproject.api.v1.models.IRequest
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.mappers.v1.fromTransport
import ru.otus.otuskotlin.myproject.mappers.v1.toTransport

@Component
class DevControllerV1Ws(private val appSettings: DevAppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> {

        val devSess = SpringWsSessionV1(session)
        sessions.add(devSess)
        val messageObj = flow {
            emit(process("ws-v1-init") {
                command = DevCommand.INIT
                wsSession = devSess
            })
        }

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v1-handle") {
                    wsSession = devSess
                    val request = apiV1Mapper.readValue(message.payloadAsText, IRequest::class.java)
                    fromTransport(request)
                }
            }

        val output = merge(messageObj, messages)
            .onCompletion {
                process("ws-v1-finish") {
                    wsSession = devSess
                    command = DevCommand.FINISH
                }
                sessions.remove(devSess)
            }
            .map { session.textMessage(apiV1Mapper.writeValueAsString(it)) }
            .asFlux()
        return session.send(output)
    }

    private suspend fun process(logId: String, function: DevContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = DevContext::toTransport,
        clazz = this@DevControllerV1Ws::class,
        logId = logId,
    )
}
