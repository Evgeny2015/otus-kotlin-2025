package ru.otus.otuskotlin.myproject.mappers.v1

import ru.otus.otuskotlin.myproject.api.v1.models.*
import ru.otus.otuskotlin.myproject.common.*
import ru.otus.otuskotlin.myproject.common.exceptions.UnknownDevCommand
import ru.otus.otuskotlin.myproject.common.models.*

fun DevContext.toTransport(): IResponse = when (val cmd = command) {
    DevCommand.CREATE -> toTransportCreate()
    DevCommand.READ -> toTransportRead()
    DevCommand.UPDATE -> toTransportUpdate()
    DevCommand.DELETE -> toTransportDelete()
    DevCommand.SEARCH -> toTransportSearch()
    DevCommand.NONE -> throw UnknownDevCommand(cmd)
}

fun DevContext.toTransportCreate() = DevCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    dev = devResponse.toTransport(),
)

fun DevContext.toTransportRead() = DevReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    dev = devResponse.toTransport()
)

fun DevContext.toTransportUpdate() = DevUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    dev = devResponse.toTransport()
)

fun DevContext.toTransportDelete() = DevDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    dev = devResponse.toTransport()
)

fun DevContext.toTransportSearch() = DevSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ads = devsResponse.toTransport()
)

fun List<DevAd>.toTransport(): List<DevResponseDevice>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun DevAd.toTransport(): DevResponseDevice = DevResponseDevice(
    id = id.toTransport(),
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.toTransport(),
    roomId = roomId.toTransport(),
    deviceStatus = deviceStatus.toTransport(),
    configuration = configuration.takeIf { it.isNotBlank() },
    manufacturer = manufacturer.takeIf { it.isNotBlank() },
    model = model.takeIf { it.isNotBlank() },
    visibility = visibility.toTransport(),
    permissions = permissionsClient.toTransport(),
)

internal fun DevId.toTransport() = takeIf { it != DevId.NONE }?.asString()
internal fun DevRoomId.toTransport() = takeIf { it != DevRoomId.NONE }?.asString()

private fun Set<DevPermissionClient>.toTransport(): Set<DevPermissions>? = this
    .map { it.toTransport() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun DevPermissionClient.toTransport() = when (this) {
    DevPermissionClient.READ -> DevPermissions.READ
    DevPermissionClient.UPDATE -> DevPermissions.UPDATE
    DevPermissionClient.MAKE_VISIBLE_OWNER -> DevPermissions.MAKE_VISIBLE_OWN
    DevPermissionClient.MAKE_VISIBLE_GROUP -> DevPermissions.MAKE_VISIBLE_GROUP
    DevPermissionClient.MAKE_VISIBLE_PUBLIC -> DevPermissions.MAKE_VISIBLE_PUBLIC
    DevPermissionClient.DELETE -> DevPermissions.DELETE
}

internal fun DeviceVisibility.toTransport(): DevVisibility? = when (this) {
    DeviceVisibility.VISIBLE_PUBLIC -> DevVisibility.PUBLIC
    DeviceVisibility.VISIBLE_TO_GROUP -> DevVisibility.REGISTERED_ONLY
    DeviceVisibility.VISIBLE_TO_OWNER -> DevVisibility.OWNER_ONLY
    DeviceVisibility.NONE -> null
}

internal fun List<DevError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun DevError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

internal fun DevType.toTransport(): DeviceType? = when (this) {
    DevType.HUB -> DeviceType.HUB
    DevType.DEVICE -> DeviceType.DEVICE
    DevType.CAMERA -> DeviceType.CAMERA
    DevType.SENSOR -> DeviceType.SENSOR
    DevType.NONE -> null
}

internal fun DevStatus.toTransport(): DeviceStatus? = when (this) {
    DevStatus.ONLINE -> DeviceStatus.ONLINE
    DevStatus.OFFLINE -> DeviceStatus.OFFLINE
    DevStatus.ERROR -> DeviceStatus.ERROR
    DevStatus.NONE -> null
}

internal fun DevState.toResult(): ResponseResult? = when (this) {
    DevState.RUNNING -> ResponseResult.SUCCESS
    DevState.FAILING -> ResponseResult.ERROR
    DevState.FINISHING -> ResponseResult.SUCCESS
    DevState.NONE -> null
}
