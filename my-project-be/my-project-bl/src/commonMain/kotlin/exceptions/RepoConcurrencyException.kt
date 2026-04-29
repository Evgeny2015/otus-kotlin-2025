package ru.otus.otuskotlin.myproject.common.repo.exceptions

import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevLock

class RepoConcurrencyException(id: DevId, expectedLock: DevLock, actualLock: DevLock?) : RepoDevException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)