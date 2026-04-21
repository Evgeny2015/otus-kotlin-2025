package ru.otus.otuskotlin.myproject.bl.validation

import ru.otus.otuskotlin.myproject.cor.ICorChainDsl
import ru.otus.otuskotlin.myproject.cor.worker
import ru.otus.otuskotlin.myproject.common.helpers.errorValidation
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.helpers.fail

fun ICorChainDsl<DevContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке.
        Отказываем в публикации заголовков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { devValidating.name.isNotEmpty() && ! devValidating.name.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "name",
            violationCode = "noContent",
            description = "field name contain letters"
        )
        )
    }
}
