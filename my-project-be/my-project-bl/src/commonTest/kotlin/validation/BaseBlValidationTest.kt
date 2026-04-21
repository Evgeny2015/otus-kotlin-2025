package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory
import ru.otus.otuskotlin.myproject.repo.common.DevRepoInitialized
import ru.otus.otuskotlin.myproject.stubs.DevStub

abstract class BaseBlValidationTest {
    protected abstract val command: DevCommand
    private val settings by lazy { DevCorSettings() }
    protected val processor by lazy { DevProcessor(settings) }

    private val repo = DevRepoInitialized(
        repo = DevRepoInMemory(),
        initObjects = listOf(
            DevStub.get(),
        ),
    )
}
