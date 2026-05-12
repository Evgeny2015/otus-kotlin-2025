package ru.otus.otuskotlin.project.e2e.be.docker

import ru.otus.otuskotlin.project.e2e.be.base.AbstractDockerCompose

object WiremockDockerCompose : AbstractDockerCompose(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
