package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.DevCommand

abstract class BaseBlValidationTest {
    protected abstract val command: DevCommand
    private val settings by lazy { DevCorSettings() }
    protected val processor by lazy { DevProcessor(settings) }
}
