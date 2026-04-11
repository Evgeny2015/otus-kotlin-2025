plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.myproject.app.kafka.MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.otus.otuskotlin.myproject.libs:my-project-lib-logging-logback")

    implementation(projects.myProjectAppCommon)

    // transport models
    implementation(projects.myProjectCommon)
    implementation(projects.myProjectApiV1Jackson)
    implementation(projects.myProjectApiV1Mappers)
    implementation(projects.myProjectApiV2Kmp)

    // logic
    implementation(projects.myProjectBl)

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}

