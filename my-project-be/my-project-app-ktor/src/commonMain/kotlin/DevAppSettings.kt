package ru.otus.otuskotlin.myproject.app.ktor

import ru.otus.otuskotlin.myproject.app.common.IDevAppSettings
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings

data class DevAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: DevCorSettings = DevCorSettings(),
    override val processor: DevProcessor = DevProcessor(corSettings),
): IDevAppSettings
