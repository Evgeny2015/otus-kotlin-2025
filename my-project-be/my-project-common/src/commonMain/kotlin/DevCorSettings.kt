package ru.otus.otuskotlin.myproject.common
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.common.ws.IDevWsSessionRepo
import ru.otus.otuskotlin.myproject.logging.common.DevLoggerProvider

data class DevCorSettings (
    val loggerProvider: DevLoggerProvider = DevLoggerProvider(),
    val wsSessions: IDevWsSessionRepo = IDevWsSessionRepo.NONE,
    val repoStub: IRepoDev = IRepoDev.NONE,
    val repoTest: IRepoDev = IRepoDev.NONE,
    val repoProd: IRepoDev = IRepoDev.NONE,
    ) {
    companion object {
        val NONE = DevCorSettings()
    }
}