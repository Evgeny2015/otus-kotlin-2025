package ru.otus.otuskotlin.myproject.app.ktor.v1

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.myproject.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseSerialize
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
        // Manually deserialize the request body using apiV1Mapper (Jackson)
        // instead of relying on ContentNegotiation
        val body = this@processV1.receive<String>()
        val request = apiV1RequestDeserialize<Q>(body)
        fromTransport(request)
    },
    {
        // Manually serialize the response using apiV1Mapper (Jackson)
        // instead of relying on ContentNegotiation
        val responseBody = apiV1ResponseSerialize(toTransport())
        this@processV1.respondText(responseBody, ContentType.Application.Json)
    },
    clazz,
    logId,
)
