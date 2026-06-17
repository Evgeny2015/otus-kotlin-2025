package ru.otus.otuskotlin.project.e2e.be.scenarios.v2.base

import ru.otus.otuskotlin.myproject.api.v2.models.*

val someCreateDev = DevCreateDevice(
    name = "Device 01",
    deviceType = DeviceType.DEVICE,
    visibility = DevVisibility.PUBLIC
)
