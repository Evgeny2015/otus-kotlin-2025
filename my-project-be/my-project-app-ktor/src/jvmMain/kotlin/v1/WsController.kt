package ru.otus.otuskotlin.myproject.app.ktor.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.myproject.api.v1.apiV1Mapper
import ru.otus.otuskotlin.myproject.api.v1.models.IRequest
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.app.ktor.base.KtorWsSessionV1
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.mappers.v1.fromTransport
import ru.otus.otuskotlin.myproject.mappers.v1.toTransport
import ru.otus.otuskotlin.myproject.mappers.v1.toTransportInit
import kotlin.reflect.KClass

private val clWsV1: KClass<*> = WebSocketSession::wsHandlerV1::class

suspend fun WebSocketSession.wsHandlerV1(appSettings: DevAppSettings) = with(KtorWsSessionV1(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = DevCommand.INIT
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) },
        clWsV1,
        "wsV1-init"
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull {
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                {
                    fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText()))
                    wsSession = this@with
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransport())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                },
                clWsV1,
                "wsV1-handle"
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this@with)
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = DevCommand.FINISH
                    wsSession = this@with
                },
                { },
                clWsV1,
                "wsV1-finish"
            )
            sessions.remove(this@with)
        }
    }.collect()
}
