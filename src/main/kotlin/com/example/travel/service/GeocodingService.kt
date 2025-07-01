package com.example.travel.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.minutes

@Service
class GeocodingService {
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

    private val apiKey = "b6b5ecd9a36e4a678c0d507fff46289a" // Replace with your Geoapify API key
    private val baseUrl = "https://api.geoapify.com/v1/geocode"

    @Serializable
    data class GeocodingResponse(
        val features: List<Feature>
    )

    @Serializable
    data class Feature(
        val properties: Properties
    )

    @Serializable
    data class Properties(
        val lat: Double,
        val lon: Double
    )

    suspend fun getCoordinates(city: String): Pair<Double?, Double?> {
        return try {
            val response = client.get("$baseUrl/search") {
                parameter("text", city)
                parameter("apiKey", apiKey)
                parameter("limit", 1)
            }.body<GeocodingResponse>()
            val feature = response.features.firstOrNull()
            if (feature != null) {
                val lat = feature.properties.lat
                val lon = feature.properties.lon
                println("Coordinates for $city: lat=$lat, lon=$lon")
                lat to lon
            } else {
                println("No coordinates found for $city")
                null to null
            }
        } catch (e: Exception) {
            println("Error fetching coordinates for $city: ${e.message}")
            null to null
        }
    }
}