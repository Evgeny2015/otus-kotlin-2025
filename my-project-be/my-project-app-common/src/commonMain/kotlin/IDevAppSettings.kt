package ru.otus.otuskotlin.myproject.app.common

import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevCorSettings

interface IDevAppSettings {
    val processor: DevProcessor
    val corSettings: DevCorSettings
}
