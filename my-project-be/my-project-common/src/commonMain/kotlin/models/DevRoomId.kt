package ru.otus.otuskotlin.myproject.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class DevRoomId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DevRoomId("")
    }
}
