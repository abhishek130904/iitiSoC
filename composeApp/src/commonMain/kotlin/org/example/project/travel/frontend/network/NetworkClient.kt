package org.example.project.travel.frontend.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Singleton shared HttpClient for the entire app.
 *
 * All network services should use [client] instead of creating their own HttpClient instances.
 * This prevents resource leaks (unclosed connections, thread pools) and ensures consistent
 * configuration across all API calls.
 *
 * The engine is resolved automatically at runtime based on the platform dependency
 * (OkHttp for Android, Darwin for iOS, etc.).
 */
object NetworkClient {
    val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
        }
    }
}
