package com.example.travel.network


import com.example.travel.dto.FlightDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class FlightApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun searchFlights(
        fromCity: String,
        toCity: String,
        date: String,
        adults: Int,
        children: Int,
        infants: Int,
        cabinClass: String
    ): List<FlightDTO> {
        return client.get("http://localhost:8080/flights/search") {
            parameter("fromCity", fromCity)
            parameter("toCity", toCity)
            parameter("date", date)
            parameter("adults", adults)
            parameter("children", children)
            parameter("infants", infants)
            parameter("cabinClass", cabinClass)
        }.body()
    }
}