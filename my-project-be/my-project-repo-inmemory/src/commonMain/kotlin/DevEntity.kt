package ru.otus.otuskotlin.myproject.repo.inmemory

import ru.otus.otuskotlin.myproject.common.models.*

data class DevEntity(
    val id: String? = null,
    val name: String? = null,
    val ownerId: String? = null,
    val devType: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: DevAd): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        devType = model.deviceType.takeIf { it != DevType.NONE }?.name,
        visibility = model.visibility.takeIf { it != DeviceVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = DevAd(
        id = id?.let { DevId(it) }?: DevId.NONE,
        name = name?: "",
        ownerId = ownerId?.let { DevUserId(it) }?: DevUserId.NONE,
        deviceType = devType?.let { DevType.valueOf(it) }?: DevType.NONE,
        visibility = visibility?.let { DeviceVisibility.valueOf(it) }?: DeviceVisibility.NONE,
        lock = lock?.let { DevLock(it) } ?: DevLock.NONE,
    )
}
