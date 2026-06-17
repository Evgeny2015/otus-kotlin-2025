package ru.otus.otuskotlin.myproject.bl.repo

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.repo.DbDevRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErrWithData
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == DevState.RUNNING }
    handle {
        val request = DbDevRequest(devRepoPrepare)
        when(val result = devRepo.updateDev(request)) {
            is DbDevResponseOk -> devRepoDone = result.data
            is DbDevResponseErr -> fail(result.errors)
            is DbDevResponseErrWithData -> {
                fail(result.errors)
                devRepoDone = result.data
            }
        }
    }
}
