package com.example.travel.network

import com.example.travel.model.dto.AccommodationDTO
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

class HotelApiClient(private val httpClient: HttpClient) {
    suspend fun getHotels(city: String): List<AccommodationDTO> {
        return httpClient.get("http://10.34.60.173:8080/api/hotels?city=$city").body()
    }
}