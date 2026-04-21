package ru.otus.otuskotlin.myproject.common.ws

interface IDevWsSessionRepo {
    fun add(session: IMkplWsSession)
    fun clearAll()
    fun remove(session: IMkplWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IDevWsSessionRepo {
            override fun add(session: IMkplWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IMkplWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
