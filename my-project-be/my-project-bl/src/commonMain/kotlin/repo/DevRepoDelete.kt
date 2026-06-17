package ru.otus.otuskotlin.myproject.bl.repo

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.repo.DbDevIdRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseErrWithData
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == DevState.RUNNING }
    handle {
        val request = DbDevIdRequest(devRepoPrepare)
        when(val result = devRepo.deleteDev(request)) {
            is DbDevResponseOk -> devRepoDone = result.data
            is DbDevResponseErr -> {
                fail(result.errors)
                devRepoDone = devRepoRead
            }
            is DbDevResponseErrWithData -> {
                fail(result.errors)
                devRepoDone = result.data
            }
        }
    }
}
