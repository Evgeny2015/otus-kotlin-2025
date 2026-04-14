package ru.otus.otuskotlin.myproject.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.myproject.app.spring.config.DevAppSettings
import ru.otus.otuskotlin.myproject.api.v2.mappers.*
import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.app.common.controllerHelper
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/ad")
class DevControllerV2Fine(private val appSettings: DevAppSettings) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: DevCreateRequest): DevCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: DevReadRequest): DevReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: DevUpdateRequest): DevUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: DevDeleteRequest): DevDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: DevSearchRequest): DevSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: DevAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransport() as R },
            clazz,
            logId,
        )
    }
}
