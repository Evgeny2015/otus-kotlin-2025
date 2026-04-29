package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.helpers.errorSystem
import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevError
import ru.otus.otuskotlin.myproject.common.models.DevLock
import ru.otus.otuskotlin.myproject.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.myproject.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: DevId) = DbDevResponseErr(
    DevError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbDevResponseErr(
    DevError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldDev: DevAd,
    expectedLock: DevLock,
    exception: Exception = RepoConcurrencyException(
        id = oldDev.id,
        expectedLock = expectedLock,
        actualLock = oldDev.lock,
    ),
) = DbDevResponseErrWithData(
    dev = oldDev,
    err = DevError(
        code = "${ru.otus.otuskotlin.myproject.common.repo.ERROR_GROUP_REPO}-concurrency",
        group = ru.otus.otuskotlin.myproject.common.repo.ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldDev.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: DevId) = DbDevResponseErr(
    DevError(
        code = "${ru.otus.otuskotlin.myproject.common.repo.ERROR_GROUP_REPO}-lock-empty",
        group = ru.otus.otuskotlin.myproject.common.repo.ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbDevResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
