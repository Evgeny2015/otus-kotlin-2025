package ru.otus.otuskotlin.project.e2e.be.docker

import ru.otus.otuskotlin.project.e2e.be.base.AbstractDockerCompose

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "docker-compose-kafka.yml"
)
