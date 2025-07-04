package com.example.travel.network

import com.example.travel.dto.CityDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger
import com.example.travel.model.dto.FlightDTO

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }

    suspend fun getCitiesWithAirports(): Result<List<CityDTO>> {
        Logger.d { "Fetching cities with airports" }
        return try {
            val response: List<CityDTO> = client.get("http://192.168.213.173:8080/api/airports/cities").body()
            Logger.d { "Fetched ${response.size} cities" }
            Result.success(response)
        } catch (e: Exception) {
            Logger.e { "Error fetching cities: ${e.message}" }
            Result.failure(e)
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
    ): Result<List<FlightDTO>> {
        Logger.d { "Searching flights: from=$fromCity, to=$toCity, date=$date" }
        return try {
            val httpResponse = client.get("http://192.168.213.173:8080/flights/search?") {
                parameter("fromCity", fromCity)
                parameter("toCity", toCity)
                parameter("date", date)
                parameter("adults", adults)
                parameter("children", children)
                parameter("infants", infants)
                parameter("cabinClass", cabinClass)
            }
            val rawResponse = httpResponse.bodyAsText()
            Logger.d { "Raw flight search response: $rawResponse" }
            val response: List<FlightDTO> = httpResponse.body()
            Logger.d { "Flight search successful: ${response.size} flights found" }
            Result.success(response)
        } catch (e: Exception) {
            Logger.e { "Flight search error: ${e.message}" }
            Result.failure(e)
        }
    }
}