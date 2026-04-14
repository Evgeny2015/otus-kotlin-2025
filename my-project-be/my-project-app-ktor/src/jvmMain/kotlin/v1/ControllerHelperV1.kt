package ru.otus.otuskotlin.myproject.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.myproject.api.v1.models.IRequest
import ru.otus.otuskotlin.myproject.api.v1.models.IResponse
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.mappers.v1.fromTransport
import ru.otus.otuskotlin.myproject.mappers.v1.toTransport
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: DevAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransport()) },
    clazz,
    logId,
)
