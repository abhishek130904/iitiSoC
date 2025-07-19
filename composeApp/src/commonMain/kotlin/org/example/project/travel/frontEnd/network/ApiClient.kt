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
import kotlinx.serialization.Serializable

const val BASE_URL = "http://10.128.70.173:8080"

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
        return httpClient.get("$BASE_URL/api/hotels?city=$city").body()
    }
}

@Serializable
data class TrainStationDTO(
    val station_code: String,
    val station_name: String
)

suspend fun fetchTrainStations(query: String): List<TrainStationDTO> {
    val url = "$BASE_URL/api/trains/stations?query=$query"
    return ApiClient.client.get(url).body()
}

@Serializable
data class TrainSearchResultDTO(
    val train_no: String,
    val train_name: String,
    val from_station_code: String,
    val from_station_name: String,
    val to_station_code: String,
    val to_station_name: String,
    val from_departure_time: String,
    val to_arrival_time: String,
    val distance: Int
)

suspend fun fetchTrains(from: String, to: String): List<TrainSearchResultDTO> {
    val url = "$BASE_URL/api/trains/search?from=$from&to=$to"
    return ApiClient.client.get(url).body()
}