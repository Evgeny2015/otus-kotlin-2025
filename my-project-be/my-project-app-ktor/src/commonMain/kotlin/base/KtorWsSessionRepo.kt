package ru.otus.otuskotlin.myproject.app.ktor.base

import ru.otus.otuskotlin.myproject.common.ws.IDevWsSession
import ru.otus.otuskotlin.myproject.common.ws.IDevWsSessionRepo

class KtorWsSessionRepo: IDevWsSessionRepo {
    private val sessions: MutableSet<IDevWsSession> = mutableSetOf()
    override fun add(session: IDevWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IDevWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
