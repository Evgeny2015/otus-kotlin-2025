package ru.otus.otuskotlin.myproject.app.kafka

import ru.otus.otuskotlin.myproject.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.myproject.api.v1.models.IRequest
import ru.otus.otuskotlin.myproject.api.v1.models.IResponse
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.mappers.v1.fromTransport
import ru.otus.otuskotlin.myproject.mappers.v1.toTransport

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: DevContext): String {
        val response: IResponse = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: DevContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
