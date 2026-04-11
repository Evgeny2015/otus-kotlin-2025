package ru.otus.otuskotlin.myproject.app.ktor.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createDev::class
suspend fun ApplicationCall.createDev(appSettings: DevAppSettings) =
    processV1<DevCreateRequest, DevCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readDev::class
suspend fun ApplicationCall.readDev(appSettings: DevAppSettings) =
    processV1<DevReadRequest, DevReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateDev::class
suspend fun ApplicationCall.updateDev(appSettings: DevAppSettings) =
    processV1<DevUpdateRequest, DevUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteDev::class
suspend fun ApplicationCall.deleteDev(appSettings: DevAppSettings) =
    processV1<DevDeleteRequest, DevDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchDev::class
suspend fun ApplicationCall.searchDev(appSettings: DevAppSettings) =
    processV1<DevSearchRequest, DevSearchResponse>(appSettings, clSearch, "search")