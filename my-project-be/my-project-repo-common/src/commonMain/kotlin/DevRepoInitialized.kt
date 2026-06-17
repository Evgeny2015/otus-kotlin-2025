package ru.otus.otuskotlin.myproject.repo.common

import ru.otus.otuskotlin.myproject.common.models.DevAd

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class DevRepoInitialized(
    private val repo: IRepoDevInitializable,
    initObjects: Collection<DevAd> = emptyList(),
) : IRepoDevInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<DevAd> = save(initObjects).toList()
}
