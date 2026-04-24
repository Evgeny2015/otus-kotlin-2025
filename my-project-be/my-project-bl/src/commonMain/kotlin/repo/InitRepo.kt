package ru.otus.otuskotlin.myproject.bl.repo

import ru.otus.otuskotlin.myproject.bl.exceptions.DevDbNotConfiguredException
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.errorSystem
import ru.otus.otuskotlin.myproject.common.helpers.fail
import ru.otus.otuskotlin.myproject.common.models.DevWorkMode
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker

fun ICorChainDsl<DevContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        devRepo = when {
            workMode == DevWorkMode.TEST -> corSettings.repoTest
            workMode == DevWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != DevWorkMode.STUB && devRepo == IRepoDev.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DevDbNotConfiguredException(workMode)
            )
        )
    }
}
