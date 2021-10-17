package dev.chieppa.config

import dev.chieppa.config.plugins.configureRouting
import dev.chieppa.config.plugins.configureSecurity
import dev.chieppa.config.plugins.configureSerialization
import dev.chieppa.config.plugins.configureTemplating
import dev.chieppa.view.createHomeRoute
import dev.chieppa.view.createLoginRoute
import dev.chieppa.view.createUserRoute
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun startServer() {
    embeddedServer(
        Netty,
        port = config.ktor.port,
        host = config.ktor.host,
        watchPaths = listOf("resources")
    ) {
        configureSecurity()
        configureRouting()
        configureTemplating()
        configureSerialization()

        createHomeRoute()
        createLoginRoute()
        if (config.users.enableNewAccounts) {
            createUserRoute()
        }

    }.start(wait = true)
}