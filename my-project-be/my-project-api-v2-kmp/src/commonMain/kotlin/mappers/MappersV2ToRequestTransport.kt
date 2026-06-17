package ru.otus.otuskotlin.myproject.api.v2.mappers

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.models.*

fun DevAd.toTransportCreate() = DevCreateDevice(
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.toTransport(),
    visibility = visibility.toTransport(),
)

fun DevAd.toTransportRead() = DevReadDevice(
    id = id.takeIf { it != DevId.NONE }?.asString(),
)

fun DevAd.toTransportUpdate() = DevUpdateDevice(
    id = id.takeIf { it != DevId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.toTransport(),
    visibility = visibility.toTransport(),
    lock = lock.takeIf { it != DevLock.NONE }?.asString(),
)

fun DevAd.toTransportDelete() = DevDeleteDevice(
    id = id.takeIf { it != DevId.NONE }?.asString(),
    lock = lock.takeIf { it != DevLock.NONE }?.asString(),
)
