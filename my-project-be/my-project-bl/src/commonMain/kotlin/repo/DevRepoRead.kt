package ru.otus.otuskotlin.myproject.biz.repo

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.repo.DbDevIdRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErrWithData
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == DevState.RUNNING }
    handle {
        val request = DbDevIdRequest(devValidated)
        when(val result = devRepo.readDev(request)) {
            is DbDevResponseOk -> devRepoRead = result.data
            is DbDevResponseErr -> fail(result.errors)
            is DbDevResponseErrWithData -> {
                fail(result.errors)
                devRepoRead = result.data
            }
        }
    }
}
