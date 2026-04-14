package ru.otus.otuskotlin.myproject.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.yaml.*
import io.ktor.server.engine.*

fun main() {
    val conf = YamlConfigLoader().load("./application.yaml")
        ?: throw RuntimeException("Cannot read application.yaml")
    println(conf)
    println("File read")

    embeddedServer(CIO,
        serverConfig {
            developmentMode = true
            module(Application::module)
        },
        configure = {
            connector {
                port = conf.port
                host = conf.host
            }
            println("Starting")
        }
    ).start(true)
}
