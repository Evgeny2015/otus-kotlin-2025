package ru.otus.otuskotlin.myproject.api.v2.mappers

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.models.*

fun DevAd.toTransportCreateDev() = DevCreateDevice(
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.toTransport(),
    roomId = roomId.toTransport(),
    deviceStatus = deviceStatus.toTransport(),
    configuration = configuration.takeIf { it.isNotBlank() },
    manufacturer = manufacturer.takeIf { it.isNotBlank() },
    model = model.takeIf { it.isNotBlank() },
    visibility = visibility.toTransport(),
)

fun DevAd.toTransportReadDev() = DevReadDevice(
    id = id.toTransport()
)

fun DevAd.toTransportUpdateDev() = DevUpdateDevice(
    id = id.toTransport(),
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.toTransport(),
    roomId = roomId.toTransport(),
    deviceStatus = deviceStatus.toTransport(),
    configuration = configuration.takeIf { it.isNotBlank() },
    manufacturer = manufacturer.takeIf { it.isNotBlank() },
    model = model.takeIf { it.isNotBlank() },
    visibility = visibility.toTransport(),
    lock = lock.toTransport(),
)

internal fun DevLock.toTransport() = takeIf { it != DevLock.NONE }?.asString()

fun DevAd.toTransportDeleteDev() = DevDeleteDevice(
    id = id.toTransport(),
    lock = lock.toTransport(),
)