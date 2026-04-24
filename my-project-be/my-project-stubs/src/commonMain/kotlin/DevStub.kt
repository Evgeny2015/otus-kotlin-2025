package ru.otus.otuskotlin.myproject.stubs

import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.stubs.DevStubs.DEV_1

object DevStub {
    fun get(): DevAd = DEV_1.copy()

    fun prepareResult(block: DevAd.() -> Unit): DevAd = get().apply(block)

    fun prepareSearchList(filter: String, type: DevType) = listOf(
        devDemand("019d59d1-d719-7f59-9a03-11a8fc0f926d", filter, type),
        devDemand("019d59d1-e1b2-7707-acee-dc8fab6e5c35", filter, type),
        devDemand("019d59d1-eae9-7044-b74e-cb349e76084d", filter, type),
        devDemand("019d59d1-f181-704c-a734-4d9967dff78d", filter, type),
        devDemand("019d59d1-f9ba-7c73-912b-00d51d8f6962", filter, type),
        devDemand("019d59d2-0dd9-7748-9d17-23c773949c90", filter, type),
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
