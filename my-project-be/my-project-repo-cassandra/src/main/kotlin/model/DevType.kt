package ru.otus.otuskotlin.myproject.backend.repo.cassandra.model

import ru.otus.otuskotlin.myproject.common.models.DevType

enum class DeviceType {
    DEVICE,
    SENSOR,
    HUB,
    CAMERA
}

fun DeviceType?.fromTransport() = when(this) {
    null -> DevType.NONE
    DeviceType.DEVICE -> DevType.DEVICE
    DeviceType.SENSOR -> DevType.SENSOR
    DeviceType.HUB -> DevType.HUB
    DeviceType.CAMERA -> DevType.CAMERA
}

fun DevType.toTransport() = when(this) {
    DevType.NONE -> null
    DevType.DEVICE -> DeviceType.DEVICE
    DevType.SENSOR -> DeviceType.SENSOR
    DevType.HUB -> DeviceType.HUB
    DevType.CAMERA -> DeviceType.CAMERA
}

