package ru.otus.otuskotlin.myproject.common.models

data class DevFilter(
    var searchString: String = "",
    var ownerId: DevUserId = DevUserId.NONE,
)
