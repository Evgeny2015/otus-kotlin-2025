package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.common.models.DevCommand
import kotlin.test.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class BlValidationCreateTest: BaseBlValidationTest() {
    override val command: DevCommand = DevCommand.CREATE

    @Test fun correctName() = validationNameCorrect(command, processor)
    @Test fun trimName() = validationNameTrim(command, processor)
    @Test fun emptyName() = validationNameEmpty(command, processor)
    @Test fun badSymbolsName() = validationNameSymbols(command, processor)

    @Test fun correctDescription() = validationDeviceTypeCorrect(command, processor)
}
