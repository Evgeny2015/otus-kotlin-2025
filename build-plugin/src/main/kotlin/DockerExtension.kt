package ru.otus.otuskotlin.myproject.plugin

import org.gradle.api.provider.MapProperty

open class DockerExtension {
    var buildContext = "./"
    var dockerFile = "Dockerfile"
    var imageTag = "latest"
    var buildArgs: MapProperty<String, String>? = null
    var noCache = false
    var removeIntermediateContainers = false

    val images = DockerImagesExtension()
}
