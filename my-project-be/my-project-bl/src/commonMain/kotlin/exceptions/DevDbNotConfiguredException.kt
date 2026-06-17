package ru.otus.otuskotlin.myproject.bl.exceptions

import ru.otus.otuskotlin.myproject.common.models.DevWorkMode

class DevDbNotConfiguredException(val workMode: DevWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
