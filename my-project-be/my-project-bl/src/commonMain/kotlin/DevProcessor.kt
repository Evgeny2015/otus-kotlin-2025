package ru.otus.otuskotlin.myproject.bl

import ru.otus.otuskotlin.myproject.bl.general.initStatus
import ru.otus.otuskotlin.myproject.bl.general.operation
import ru.otus.otuskotlin.myproject.bl.general.stubs
import ru.otus.otuskotlin.myproject.bl.stubs.stubCreateSuccess
import ru.otus.otuskotlin.myproject.bl.stubs.stubDbError
import ru.otus.otuskotlin.myproject.bl.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.myproject.bl.stubs.stubNoCase
import ru.otus.otuskotlin.myproject.bl.stubs.stubReadSuccess
import ru.otus.otuskotlin.myproject.bl.stubs.stubSearchSuccess
import ru.otus.otuskotlin.myproject.bl.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.myproject.bl.stubs.stubValidationBadDeviceType
import ru.otus.otuskotlin.myproject.bl.stubs.stubValidationBadId
import ru.otus.otuskotlin.myproject.bl.stubs.stubValidationBadName
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.DevCommand
import ru.otus.otuskotlin.myproject.cor.rootChain

class DevProcessor(val corSettings: DevCorSettings = DevCorSettings.NONE) {
    suspend fun exec(ctx: DevContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<DevContext> {
        initStatus("Инициализация статуса")

        operation("Создание объявления", DevCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadName("Имитация ошибки валидации названия")
                stubValidationBadDeviceType("Имитация ошибки валидации типа устройства")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получить объявление", DevCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Изменить объявление", DevCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadName("Имитация ошибки валидации заголовка")
                stubValidationBadDeviceType("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Удалить объявление", DevCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск объявлений", DevCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}
