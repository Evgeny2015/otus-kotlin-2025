package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.*

abstract class BaseInitDevs(private val op: String): IInitObjects<DevAd> {
    open val lockOld: DevLock = DevLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: DevLock = DevLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: DevUserId = DevUserId("owner-123"),
        devType: DevType = DevType.DEVICE,
    ) = DevAd(
        id = DevId("dev-repo-$op-$suf"),
        name = "$suf stub",
        deviceType = devType,
        ownerId = ownerId,
        visibility = DeviceVisibility.VISIBLE_TO_OWNER,
    )
}
