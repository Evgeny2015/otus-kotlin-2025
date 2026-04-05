package ru.otus.otuskotlin.myproject.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

data class DevContext(
    var command: DevCommand = DevCommand.NONE,
    var state: DevState = DevState.NONE,
    val errors: MutableList<DevError> = mutableListOf(),

    var workMode: DevWorkMode = DevWorkMode.PROD,
    var stubCase: DevStubs = DevStubs.NONE,

    var requestId: DevRequestId = DevRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var devRequest: DevAd = DevAd(),
    var devFilterRequest: DevFilter = DevFilter(),

    var devResponse: DevAd = DevAd(),
    var devsResponse: MutableList<DevAd> = mutableListOf(),
)
