package ru.otus.otuskotlin.myproject.common.ws

interface IDevWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IDevWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
