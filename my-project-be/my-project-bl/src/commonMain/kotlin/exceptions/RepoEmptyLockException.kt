package ru.otus.otuskotlin.myproject.common.repo.exceptions

import ru.otus.otuskotlin.myproject.common.models.DevId

class RepoEmptyLockException(id: DevId) : RepoDevException(
    id,
    "Lock is empty in DB"
)