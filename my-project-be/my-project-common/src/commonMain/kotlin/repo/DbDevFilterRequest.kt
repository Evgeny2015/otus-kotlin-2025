package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.models.DevUserId

data class DbDevFilterRequest(
    val nameFilter: String = "",
    val ownerId: DevUserId = DevUserId.NONE,
    val devType: DevType = DevType.NONE,
)
