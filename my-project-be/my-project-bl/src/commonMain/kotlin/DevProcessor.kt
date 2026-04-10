package ru.otus.otuskotlin.myproject.bl

import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.DevState
import ru.otus.otuskotlin.myproject.stubs.DevStub

@Suppress("unused", "RedundantSuspendModifier")
class DevProcessor(val corSettings: DevCorSettings) {
    suspend fun exec(ctx: DevContext) {
        ctx.devResponse = DevStub.get()
        ctx.state = DevState.RUNNING
    }
}
