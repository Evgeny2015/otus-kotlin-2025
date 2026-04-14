package ru.otus.otuskotlin.myproject.app.ktor.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createDev::class
suspend fun ApplicationCall.createDev(appSettings: DevAppSettings) =
    processV2<DevCreateRequest, DevCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readDev::class
suspend fun ApplicationCall.readDev(appSettings: DevAppSettings) =
    processV2<DevReadRequest, DevReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateDev::class
suspend fun ApplicationCall.updateDev(appSettings: DevAppSettings) =
    processV2<DevUpdateRequest, DevUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteDev::class
suspend fun ApplicationCall.deleteDev(appSettings: DevAppSettings) =
    processV2<DevDeleteRequest, DevDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchDev::class
suspend fun ApplicationCall.searchDev(appSettings: DevAppSettings) =
    processV2<DevSearchRequest, DevSearchResponse>(appSettings, clSearch, "search")
