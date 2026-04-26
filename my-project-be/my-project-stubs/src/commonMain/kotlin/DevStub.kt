package ru.otus.otuskotlin.myproject.stubs

import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.stubs.DevStubs.DEV_1

object DevStub {
    fun get(): DevAd = DEV_1.copy()

    fun prepareResult(block: DevAd.() -> Unit): DevAd = get().apply(block)

    fun prepareSearchList(filter: String, type: DevType) = listOf(
        devDemand("01", filter, type),
        devDemand("02", filter, type),
        devDemand("03", filter, type),
        devDemand("04", filter, type),
        devDemand("05", filter, type),
        devDemand("06", filter, type),
    )

    private fun devDemand(id: String, filter: String, type: DevType) =
        devAd(DEV_1, id = id, filter = filter, type = type)

    private fun devSupply(id: String, filter: String, type: DevType) =
        devAd(DEV_1, id = id, filter = filter, type = type)

    private fun devAd(base: DevAd, id: String, filter: String, type: DevType) = base.copy(
        id = DevId(id),
        name = "$filter $id",
        deviceType = type,
    )

}
