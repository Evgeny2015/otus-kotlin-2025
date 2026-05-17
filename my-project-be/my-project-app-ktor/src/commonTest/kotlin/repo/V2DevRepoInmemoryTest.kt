package ru.otus.otuskotlin.myproject.app.ktor.repo

import ru.otus.otuskotlin.myproject.api.v2.models.DevRequestDebugMode
import ru.otus.otuskotlin.myproject.app.ktor.DevAppSettings
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.repo.common.DevRepoInitialized
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory

class V2DevRepoInmemoryTest : V2DevRepoBaseTest() {
    override val workMode: DevRequestDebugMode = DevRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoDev) = DevAppSettings(
        corSettings = DevCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: DevAppSettings = mkAppSettings(
        repo = DevRepoInitialized(DevRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: DevAppSettings = mkAppSettings(
        repo = DevRepoInitialized(
            DevRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDev),
        )
    )
    override val appSettingsUpdate: DevAppSettings = mkAppSettings(
        repo = DevRepoInitialized(
            DevRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDev),
        )
    )
    override val appSettingsDelete: DevAppSettings = mkAppSettings(
        repo = DevRepoInitialized(
            DevRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDev),
        )
    )
    override val appSettingsSearch: DevAppSettings = mkAppSettings(
        repo = DevRepoInitialized(
            DevRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initDev),
        )
    )
}
