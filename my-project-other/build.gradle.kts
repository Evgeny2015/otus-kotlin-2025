plugins {
    id("build-jvm")
    id("maven-publish")
}

group = "ru.otus.otuskotlin.myproject"
version = "0.0.1"

base {
    archivesName.set("dcompose")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("buildInfra") {
        group = "build"
        dependsOn(project("my-project-dcompose").getTasksByName("publish",false))
        dependsOn(project("my-project-migration-cs").getTasksByName("buildImages",false))
    }
}
