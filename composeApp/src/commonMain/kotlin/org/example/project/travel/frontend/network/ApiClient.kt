package com.example.travel.network

import com.example.travel.model.dto.AccommodationDTO
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}

class HotelApiClient(private val client: HttpClient) {
    suspend fun getHotelsByCity(city: String): List<AccommodationDTO> {
        return client.get("http://192.168.18.251:8080/api/hotels?") {
            parameter("city", city)
        }.body()
    }
}