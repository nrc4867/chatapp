package dev.chieppa

import dev.chieppa.config.connectToDatabase
import dev.chieppa.config.setupConfig
import dev.chieppa.config.startServer

fun main(args: Array<String>) {
    setupConfig(args)
    connectToDatabase()
    startServer()
}