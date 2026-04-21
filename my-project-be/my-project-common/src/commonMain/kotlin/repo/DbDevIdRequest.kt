package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevId

data class DbDevIdRequest(
    val id: DevId,
) {
    constructor(dev: DevAd): this(dev.id)
}
