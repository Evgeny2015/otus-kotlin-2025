package ru.otus.otuskotlin.myproject.common.exceptions

import ru.otus.otuskotlin.myproject.common.models.DevCommand


class UnknownDevCommand(command: DevCommand) : Throwable("Wrong command $command at mapping toTransport stage")
