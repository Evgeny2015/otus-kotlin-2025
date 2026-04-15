package ru.otus.otuskotlin.myproject.common.models

data class DevFilter(
    var searchString: String = "",
    var ownerId: DevUserId = DevUserId.NONE,
) {
    fun deepCopy(): DevFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = DevFilter()
    }
}
