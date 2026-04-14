package ru.otus.otuskotlin.myproject.app.spring.config

import ru.otus.otuskotlin.myproject.app.common.IDevAppSettings
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings

data class DevAppSettings(
    override val corSettings: DevCorSettings,
    override val processor: DevProcessor,
): IDevAppSettings
