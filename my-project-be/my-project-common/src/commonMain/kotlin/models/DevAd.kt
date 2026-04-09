package ru.otus.otuskotlin.myproject.common.models

data class DevAd(
    var id: DevId = DevId.NONE,
    var name: String = "",
    var ownerId: DevUserId = DevUserId.NONE,
    var deviceType: DevType = DevType.NONE,
    var roomId: DevRoomId = DevRoomId.NONE,
    var deviceStatus: DevStatus = DevStatus.NONE,
    var lock: DevLock = DevLock.NONE,
    var configuration: String = "",
    var lastSeen: String = "",
    var manufacturer: String = "",
    var model: String = "",
    var visibility: DeviceVisibility = DeviceVisibility.NONE,
    val permissionsClient: MutableSet<DevPermissionClient> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = DevAd()
    }
}
