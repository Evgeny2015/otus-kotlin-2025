package ru.otus.otuskotlin.project.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.otus.otuskotlin.project.e2e.be.base.BaseContainerTest
import ru.otus.otuskotlin.project.e2e.be.base.client.Client
import ru.otus.otuskotlin.project.e2e.be.base.client.RestClient
import ru.otus.otuskotlin.project.e2e.be.docker.KtorJvmCsDockerCompose
import ru.otus.otuskotlin.project.e2e.be.scenarios.v1.ScenariosV1
import ru.otus.otuskotlin.project.e2e.be.scenarios.v2.ScenariosV2
import ru.otus.otuskotlin.myproject.api.v1.models.DevDebug as DevDebugV1
import ru.otus.otuskotlin.myproject.api.v1.models.DevRequestDebugMode as DevRequestDebugModeV1
import ru.otus.otuskotlin.myproject.api.v2.models.DevDebug as DevDebugV2
import ru.otus.otuskotlin.myproject.api.v2.models.DevRequestDebugMode as DevRequestDebugModeV2

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCsJvm: BaseContainerTest(KtorJvmCsDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, DevDebugV1(mode = DevRequestDebugModeV1.PROD))
    @Nested
    internal inner class V2: ScenariosV2(client, DevDebugV2(mode = DevRequestDebugModeV2.PROD))

}