package ru.otus.otuskotlin.myproject.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.common.stubs.DevStubs

data class DevContext(
    var command: DevCommand = DevCommand.NONE,
    var state: DevState = DevState.NONE,
    val errors: MutableList<DevError> = mutableListOf(),

    var corSettings: DevCorSettings = DevCorSettings(),
    var workMode: DevWorkMode = DevWorkMode.PROD,
    var stubCase: DevStubs = DevStubs.NONE,

    var requestId: DevRequestId = DevRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var devRequest: DevAd = DevAd(),
    var devFilterRequest: DevFilter = DevFilter(),

    var devValidating: DevAd = DevAd(),
    var devFilterValidating: DevFilter = DevFilter(),

    var devValidated: DevAd = DevAd(),
    var devFilterValidated: DevFilter = DevFilter(),

    var devRepo: IRepoDev = IRepoDev.NONE,
    var devRepoRead: DevAd = DevAd(), // То, что прочитали из репозитория
    var devRepoPrepare: DevAd = DevAd(), // То, что готовим для сохранения в БД
    var devRepoDone: DevAd = DevAd(),  // Результат, полученный из БД
    var devsRepoDone: MutableList<DevAd> = mutableListOf(),

    var devResponse: DevAd = DevAd(),
    var devsResponse: MutableList<DevAd> = mutableListOf(),
)
