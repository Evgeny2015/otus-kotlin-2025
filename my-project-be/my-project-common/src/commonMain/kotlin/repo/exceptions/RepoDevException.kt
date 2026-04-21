package ru.otus.otuskotlin.myproject.common.repo.exceptions

import ru.otus.otuskotlin.myproject.common.models.DevId

open class RepoDevException(
    @Suppress("unused")
    val adId: DevId,
    msg: String,
): RepoException(msg)
