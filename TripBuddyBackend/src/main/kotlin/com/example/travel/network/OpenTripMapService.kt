package com.example.travel.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.minutes

@Service
class OpenTripMapService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1.minutes.inWholeMilliseconds
        }
    }

    private val apiKey = "5ae2e3f221c38a28845f05b602aac280c9dcc40bee271eee00bd2803" // Replace with your API key
    private val baseUrl = "https://api.opentripmap.com/0.1/en"

    @Serializable
    data class OpenTripMapResponse(
        val features: List<Feature>
    )

    @Serializable
    data class Feature(
        val properties: Properties
    )

    @Serializable
    data class Properties(
        val name: String,
        val kinds: String,
        val lat: Double? = null,
        val lon: Double? = null
    )

    fun getActivitiesForCity(city: String, lat: Double, lon: Double): List<Feature> {
        return runBlocking {
            client.get("$baseUrl/places/radius") {
                parameter("radius", "30000")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("apikey", apiKey)
                parameter("limit", 50)
            }.body<OpenTripMapResponse>().features
        }
    }
}