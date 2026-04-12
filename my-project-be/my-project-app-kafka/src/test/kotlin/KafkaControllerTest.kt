package ru.otus.otuskotlin.myproject.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.myproject.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.myproject.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.myproject.api.v1.models.DevCreateDevice
import ru.otus.otuskotlin.myproject.api.v1.models.DevCreateRequest
import ru.otus.otuskotlin.myproject.api.v1.models.DevCreateResponse
import ru.otus.otuskotlin.myproject.api.v1.models.DevDebug
import ru.otus.otuskotlin.myproject.api.v1.models.DevRequestDebugMode
import ru.otus.otuskotlin.myproject.api.v1.models.DevRequestDebugStubs
import ru.otus.otuskotlin.myproject.api.v1.models.DevVisibility
import ru.otus.otuskotlin.myproject.api.v1.models.DeviceStatus
import ru.otus.otuskotlin.myproject.api.v1.models.DeviceType
import ru.otus.otuskotlin.myproject.stubs.DevStub
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        DevCreateRequest(
                            dev = DevCreateDevice(
                                name = "Требуется болт",
                                deviceType = DeviceType.DEVICE,
                                deviceStatus = DeviceStatus.ONLINE,
                                visibility = DevVisibility.OWNER_ONLY,
                            ),
                            debug = DevDebug(
                                mode = DevRequestDebugMode.STUB,
                                stub = DevRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<DevCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals(DevStub.get().name, result.dev?.name)
    }

    companion object {
        const val PARTITION = 0
    }
}


