package ru.otus.otuskotlin.myproject.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.myproject.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransport
import ru.otus.otuskotlin.myproject.api.v2.models.IRequest
import ru.otus.otuskotlin.myproject.api.v2.models.IResponse
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: DevAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(this@processV2.receive<Q>())
    },
    { this@processV2.respond(toTransport() as R) },
    clazz,
    logId,
)
