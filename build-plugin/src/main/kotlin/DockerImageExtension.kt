package ru.otus.otuskotlin.myproject.plugin

class DockerImageExtension {
    var name: String? = null
    var buildContext = "./"
    var dockerFile = "Dockerfile"
    var imageTag = "latest"
    var dependsOnTask: String? = null
    var buildArgs: Map<String, String> = mapOf()
    var noCache = false
}
