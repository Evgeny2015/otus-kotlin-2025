package ru.otus.otuskotlin.myproject.backend.repo.tests

import ru.otus.otuskotlin.myproject.common.models.*

abstract class BaseInitDevs(private val op: String): IInitObjects<DevAd> {
    fun createInitTestModel(
        suf: String,
        ownerId: DevUserId = DevUserId("owner-123"),
        devType: DevType = DevType.DEVICE,
    ) = DevAd(
        id = DevId("ad-repo-$op-$suf"),
        name = "$suf stub",
        deviceType = devType,
        ownerId = ownerId,
        visibility = DeviceVisibility.VISIBLE_TO_OWNER,
    )
}
