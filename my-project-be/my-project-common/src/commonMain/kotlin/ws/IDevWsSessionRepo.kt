package ru.otus.otuskotlin.myproject.common.ws

interface IDevWsSessionRepo {
    fun add(session: IDevWsSession)
    fun clearAll()
    fun remove(session: IDevWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IDevWsSessionRepo {
            override fun add(session: IDevWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IDevWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
