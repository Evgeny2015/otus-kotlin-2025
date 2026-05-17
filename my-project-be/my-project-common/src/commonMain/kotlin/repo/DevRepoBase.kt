package ru.otus.otuskotlin.myproject.common.repo

import ru.otus.otuskotlin.myproject.common.helpers.errorSystem

abstract class DevRepoBase: IRepoDev {

    protected suspend fun tryDevMethod(block: suspend () -> IDbDevResponse) = try {
        block()
    } catch (e: Throwable) {
        DbDevResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryDevsMethod(block: suspend () -> IDbDevsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbDevsResponseErr(errorSystem("methodException", e = e))
    }

}
