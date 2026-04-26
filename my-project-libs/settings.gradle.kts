rootProject.name = "myproject-libs"

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
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("my-project-lib-logging-common")
include("my-project-lib-logging-logback")
include("my-project-lib-logging-kermit")
include("my-project-lib-logging-socket")
include("my-project-lib-cor")
