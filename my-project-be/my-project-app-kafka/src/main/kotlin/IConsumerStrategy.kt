package ru.otus.otuskotlin.myproject.app.kafka

import ru.otus.otuskotlin.myproject.common.DevContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: DevContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: DevContext)
}
