package ru.otus.otuskotlin.myproject.app.kafka

import ru.otus.otuskotlin.myproject.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.myproject.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.myproject.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.myproject.api.v2.mappers.toTransport
import ru.otus.otuskotlin.myproject.api.v2.models.IRequest
import ru.otus.otuskotlin.myproject.api.v2.models.IResponse
import ru.otus.otuskotlin.myproject.common.DevContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: DevContext): String {
        val response: IResponse = source.toTransport()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: DevContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
