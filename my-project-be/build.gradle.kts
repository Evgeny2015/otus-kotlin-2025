plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

//group = libs.versions.project.group.get()
group = "ru.otus.otuskotlin.myproject"
version = "0.0.1"

repositories {
    mavenCentral()
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-v2.yaml").toString())
    set("spec-dev-log", specDir.file("specs-dev-log.yaml").toString())
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }
}