package ru.otus.otuskotlin.myproject.bl.repo

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.common.repo.DbDevFilterRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevsResponseErr
import ru.otus.otuskotlin.myproject.common.repo.DbDevsResponseOk
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == DevState.RUNNING }
    handle {
        val request = DbDevFilterRequest(
            nameFilter = devFilterValidated.searchString,
            ownerId = devFilterValidated.ownerId,
            devType = devFilterValidated.devType
        )
        when(val result = devRepo.searchDev(request)) {
            is DbDevsResponseOk -> devsRepoDone = result.data.toMutableList()
            is DbDevsResponseErr -> fail(result.errors)
        }
    }
}
