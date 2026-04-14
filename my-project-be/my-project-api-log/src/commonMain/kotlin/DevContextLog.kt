package ru.otus.otuskotlin.myproject.api.log.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.myproject.api.log.models.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.models.*

fun DevContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "my-project",
    ad = toDevLog(),
    errors = errors.map { it.toLog() },
)

private fun DevContext.toDevLog(): DevLogModel? {
    val adNone = DevAd()
    return DevLogModel(
        requestId = requestId.takeIf { it != DevRequestId.NONE }?.asString(),
        requestDev = devRequest.takeIf { it != adNone }?.toLog(),
        responseDev = devResponse.takeIf { it != adNone }?.toLog(),
        responseDevs = devsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = devFilterRequest.takeIf { it != DevFilter() }?.toLog(),
    ).takeIf { it != DevLogModel() }
}

private fun DevFilter.toLog() = DevFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != DevUserId.NONE }?.asString(),
)

private fun DevError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun DevAd.toLog() = DevLog(
    id = id.takeIf { it != DevId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    deviceType = deviceType.takeIf { it != DevType.NONE }?.name,
    roomId = roomId.takeIf { it != DevRoomId.NONE }?.asString(),
    deviceStatus = deviceStatus.takeIf { it != DevStatus.NONE }?.name,
    configuration = configuration.takeIf { it.isNotEmpty() },
    model = model.takeIf { it.isNotEmpty() },
    ownerId = ownerId.takeIf { it != DevUserId.NONE }?.asString(),
    visibility = visibility.takeIf { it != DeviceVisibility.NONE }?.name,
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
