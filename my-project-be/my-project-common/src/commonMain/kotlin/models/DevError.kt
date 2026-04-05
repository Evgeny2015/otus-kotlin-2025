package ru.otus.otuskotlin.myproject.common.models

data class DevError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
