package ru.otus.otuskotlin.myproject.bl

import ru.otus.otuskotlin.myproject.bl.general.initStatus
import ru.otus.otuskotlin.myproject.bl.general.operation
import ru.otus.otuskotlin.myproject.bl.general.stubs
import ru.otus.otuskotlin.myproject.bl.repo.*
import ru.otus.otuskotlin.myproject.bl.stubs.*
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.bl.validation.*
import ru.otus.otuskotlin.myproject.cor.chain
import ru.otus.otuskotlin.myproject.cor.rootChain
import ru.otus.otuskotlin.myproject.cor.worker

class DevProcessor(val corSettings: DevCorSettings = DevCorSettings.NONE) {
    suspend fun exec(ctx: DevContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<DevContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание устройства", DevCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadName("Имитация ошибки валидации названия")
                stubValidationBadDeviceType("Имитация ошибки валидации типа устройства")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в devValidating") { devValidating = devRequest.deepCopy() }
                worker("Очистка id") { devValidating.id = DevId.NONE }
                worker("Очистка заголовка") { devValidating.name = devValidating.name.trim() }
                validateNameNotEmpty("Проверка, что заголовок не пуст")
                validateNameHasContent("Проверка символов")
                validateDeviceTypeNotNone("Проверка на тип устройства")

                finishAdValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить устройство", DevCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в devValidating") { devValidating = devRequest.deepCopy() }
                worker("Очистка id") { devValidating.id = DevId(devValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishAdValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение объявления из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == DevState.RUNNING }
                    handle { devRepoDone = devRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить устройство", DevCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadName("Имитация ошибки валидации заголовка")
                stubValidationBadDeviceType("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в devValidating") { devValidating = devRequest.deepCopy() }
                worker("Очистка id") { devValidating.id = DevId(devValidating.id.asString().trim()) }
                worker("Очистка lock") { devValidating.lock = DevLock(devValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { devValidating.name = devValidating.name.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateNameNotEmpty("Проверка на непустой заголовок")
                validateNameHasContent("Проверка на наличие содержания в заголовке")
                validateDeviceTypeNotNone("Проверка на тип устройства")

                finishAdValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение объявления из БД")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить устройство", DevCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в devValidating") {
                    devValidating = devRequest.deepCopy()
                }
                worker("Очистка id") { devValidating.id = DevId(devValidating.id.asString().trim()) }
                worker("Очистка lock") { devValidating.lock = DevLock(devValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishAdValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение объявления из БД")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление объявления из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск устройств", DevCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            repoSearch("Поиск объявления в БД по фильтру")
            prepareResult("Подготовка ответа")
            validation {
                worker("Копируем поля в adFilterValidating") { devFilterValidating = devFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}
