package ru.otus.otuskotlin.myproject.repo.common

import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev

interface IRepoDevInitializable: IRepoDev {
    fun save(devs: Collection<DevAd>) : Collection<DevAd>
}
