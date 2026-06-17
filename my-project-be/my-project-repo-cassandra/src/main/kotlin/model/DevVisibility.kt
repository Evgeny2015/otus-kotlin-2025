package ru.otus.otuskotlin.myproject.backend.repo.cassandra.model

import ru.otus.otuskotlin.myproject.common.models.DeviceVisibility

enum class DevVisibility {
    VISIBLE_TO_OWNER,
    VISIBLE_TO_GROUP,
    VISIBLE_PUBLIC,
}

fun DevVisibility?.fromTransport() = when(this) {
    null -> DeviceVisibility.NONE
    DevVisibility.VISIBLE_TO_OWNER -> DeviceVisibility.VISIBLE_TO_OWNER
    DevVisibility.VISIBLE_TO_GROUP -> DeviceVisibility.VISIBLE_TO_GROUP
    DevVisibility.VISIBLE_PUBLIC -> DeviceVisibility.VISIBLE_PUBLIC
}

fun DeviceVisibility.toTransport() = when(this) {
    DeviceVisibility.NONE -> null
    DeviceVisibility.VISIBLE_TO_OWNER -> DevVisibility.VISIBLE_TO_OWNER
    DeviceVisibility.VISIBLE_TO_GROUP -> DevVisibility.VISIBLE_TO_GROUP
    DeviceVisibility.VISIBLE_PUBLIC -> DevVisibility.VISIBLE_PUBLIC
}
