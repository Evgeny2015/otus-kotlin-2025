package ru.otus.otuskotlin.myproject.stubs

import ru.otus.otuskotlin.myproject.common.models.*

object DevStubs {
    val DEV_1: DevAd
        get() = DevAd(
            id = DevId("019d49ed-9102-7cc1-9a5f-532bf2b6d863"),
            name = "Устройство  №1",
            ownerId = DevUserId("user-1"),
            deviceType = DevType.DEVICE,
            roomId = DevRoomId("019d59c6-60b1-723b-8a35-c6b721c7a08e"),
            deviceStatus = DevStatus.ONLINE,
            lock = DevLock("123"),
            permissionsClient = mutableSetOf(
                DevPermissionClient.READ,
                DevPermissionClient.UPDATE,
                DevPermissionClient.DELETE,
                DevPermissionClient.MAKE_VISIBLE_PUBLIC,
                DevPermissionClient.MAKE_VISIBLE_GROUP,
                DevPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val DEV_2 = DEV_1.copy(id = DevId("019d59ca-382c-77a3-af3e-32d8fe6f9761"))
}
