package dev.chieppa.config.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json

val jsonFormat = Json { encodeDefaults = true }

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}