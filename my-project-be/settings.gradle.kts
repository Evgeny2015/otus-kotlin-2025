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

//plugins {
    //id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
//}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("my-project-api-v1-jackson")
include("my-project-api-v1-mappers")
include("my-project-api-v2-kmp")
include("my-project-common")
include("my-project-stubs")
include("my-project-api-log")
include("my-project-bl")
include("my-project-app-common")
include("my-project-app-spring")