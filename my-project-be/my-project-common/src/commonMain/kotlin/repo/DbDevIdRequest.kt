package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevLock

data class DbDevIdRequest(
    val id: DevId,
    val lock: DevLock = DevLock.NONE
) {
    constructor(dev: DevAd): this(dev.id, dev.lock)
}
