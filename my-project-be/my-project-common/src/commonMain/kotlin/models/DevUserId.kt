package ru.otus.otuskotlin.myproject.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class DevUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DevUserId("")
    }
}
