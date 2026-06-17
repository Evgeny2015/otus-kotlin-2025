package ru.otus.otuskotlin.myproject.app.ktor.v2

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.myproject.api.v2.apiV2Mapper
import ru.otus.otuskotlin.myproject.api.v2.apiV2RequestDeserialize
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
        // Manually deserialize the request body using apiV2Mapper (Kotlinx serialization)
        // instead of relying on ContentNegotiation
        val body = this@processV2.receive<String>()
        val request = apiV2RequestDeserialize<Q>(body)
        fromTransport(request)
    },
    {
        // Manually serialize the response using apiV2Mapper (Kotlinx serialization)
        // Serialize as the concrete type R to avoid polymorphic "type" discriminator
        val responseObj = toTransport()
        val responseBody = apiV2Mapper.encodeToString(responseObj as R)
        this@processV2.respondText(responseBody, ContentType.Application.Json)
    },
    clazz,
    logId,
)
