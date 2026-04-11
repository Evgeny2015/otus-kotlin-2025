package ru.otus.otuskotlin.myproject.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings

fun Route.v2Dev(appSettings: DevAppSettings) {
    route("dev") {
        post("create") {
            call.createDev(appSettings)
        }
        post("read") {
            call.readDev(appSettings)
        }
        post("update") {
            call.updateDev(appSettings)
        }
        post("delete") {
            call.deleteDev(appSettings)
        }
        post("search") {
            call.searchDev(appSettings)
        }
    }
}
