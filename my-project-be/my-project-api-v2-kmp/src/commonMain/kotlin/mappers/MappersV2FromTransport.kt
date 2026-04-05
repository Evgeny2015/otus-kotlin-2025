package ru.otus.otuskotlin.myproject.mappers.v2

import ru.otus.otuskotlin.myproject.api.v2.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

fun DevContext.fromTransport(request: IRequest) = when (request) {
    is DevCreateRequest -> fromTransport(request)
    is DevReadRequest -> fromTransport(request)
    is DevUpdateRequest -> fromTransport(request)
    is DevDeleteRequest -> fromTransport(request)
    is DevSearchRequest -> fromTransport(request)
}

private fun String?.toDevId() = this?.let { DevId(it) } ?: DevId.NONE
private fun String?.toRoomId() = this?.let { DevRoomId(it) } ?: DevRoomId.NONE
private fun String?.toDevWithId() = DevAd(id = this.toDevId())
private fun String?.toDevLock() = this?.let { DevLock(it) } ?: DevLock.NONE

private fun DevDebug?.transportToWorkMode(): DevWorkMode = when (this?.mode) {
    DevRequestDebugMode.PROD -> DevWorkMode.PROD
    DevRequestDebugMode.TEST -> DevWorkMode.TEST
    DevRequestDebugMode.STUB -> DevWorkMode.STUB
    null -> DevWorkMode.PROD
}

private fun DevDebug?.transportToStubCase(): DevStubs = when (this?.stub) {
    DevRequestDebugStubs.SUCCESS -> DevStubs.SUCCESS
    DevRequestDebugStubs.NOT_FOUND -> DevStubs.NOT_FOUND
    DevRequestDebugStubs.BAD_ID -> DevStubs.BAD_ID
    DevRequestDebugStubs.BAD_NAME -> DevStubs.BAD_NAME
    DevRequestDebugStubs.BAD_TYPE -> DevStubs.BAD_DEVICE_TYPE
    DevRequestDebugStubs.BAD_ROOM_ID -> DevStubs.BAD_ROOM_ID
    DevRequestDebugStubs.BAD_STATUS -> DevStubs.BAD_DEVICE_STATUS
    DevRequestDebugStubs.BAD_CONFIGURATION -> DevStubs.BAD_CONFIGURATION
    DevRequestDebugStubs.CANNOT_DELETE -> DevStubs.CANNOT_DELETE
    DevRequestDebugStubs.BAD_SEARCH_STRING -> DevStubs.BAD_SEARCH_STRING
    null -> DevStubs.NONE
}

fun DevContext.fromTransport(request: DevReadRequest) {
    command = DevCommand.READ
    devRequest = request.dev.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DevReadDevice?.toInternal(): DevAd = if (this != null) {
    DevAd(id = id.toDevId())
} else {
    DevAd()
}

fun DevContext.fromTransport(request: DevCreateRequest) {
    command = DevCommand.CREATE
    devRequest = request.dev?.toInternal() ?: DevAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DevContext.fromTransport(request: DevUpdateRequest) {
    command = DevCommand.UPDATE
    devRequest = request.dev?.toInternal() ?: DevAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DevContext.fromTransport(request: DevDeleteRequest) {
    command = DevCommand.DELETE
    devRequest = request.dev.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DevDeleteDevice?.toInternal(): DevAd = if (this != null) {
    DevAd(
        id = id.toDevId(),
        lock = lock.toDevLock(),
    )
} else {
    DevAd()
}

fun DevContext.fromTransport(request: DevSearchRequest) {
    command = DevCommand.SEARCH
    devFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DevSearchFilter?.toInternal(): DevFilter = DevFilter(
    searchString = this?.searchString ?: ""
)

private fun DevCreateDevice.toInternal(): DevAd = DevAd(
    name = this.name ?: "",
    deviceType = this.deviceType.fromTransport(),
    roomId = this.roomId.toRoomId(),
    deviceStatus = this.deviceStatus.fromTransport(),
    configuration = this.configuration ?: "",
    manufacturer = this.manufacturer ?: "",
    model = this.model ?: "",
)

private fun DevUpdateDevice.toInternal(): DevAd = DevAd(
    id = this.id.toDevId(),
    name = this.name ?: "",
    deviceType = this.deviceType.fromTransport(),
    roomId = this.roomId.toRoomId(),
    deviceStatus = this.deviceStatus.fromTransport(),
    configuration = this.configuration ?: "",
    manufacturer = this.manufacturer ?: "",
    model = this.model ?: "",
    lock = lock.toDevLock(),
)

private fun DeviceType?.fromTransport(): DevType = when (this) {
    DeviceType.DEVICE -> DevType.DEVICE
    DeviceType.SENSOR -> DevType.SENSOR
    DeviceType.CAMERA -> DevType.CAMERA
    DeviceType.HUB -> DevType.HUB
    null -> DevType.NONE
}

private fun DeviceStatus?.fromTransport(): DevStatus = when (this) {
    DeviceStatus.ONLINE -> DevStatus.ONLINE
    DeviceStatus.OFFLINE -> DevStatus.OFFLINE
    DeviceStatus.ERROR -> DevStatus.ERROR
    null -> DevStatus.NONE
}

private fun DevVisibility?.fromTransport(): DeviceVisibility = when (this) {
    DevVisibility.PUBLIC -> DeviceVisibility.VISIBLE_PUBLIC
    DevVisibility.OWNER_ONLY -> DeviceVisibility.VISIBLE_TO_OWNER
    DevVisibility.REGISTERED_ONLY -> DeviceVisibility.VISIBLE_TO_GROUP
    null -> DeviceVisibility.NONE
}

