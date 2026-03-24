rootProject.name = "my-project-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
//    repositories {
//        mavenCentral()
//        gradlePluginPortal()
//    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("my-project-tmp")