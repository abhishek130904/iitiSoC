package org.example.project.travel.frontend.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.travel.frontEnd.model.TripHistoryRequest
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.model.*
import com.example.travel.network.BASE_URL
import org.example.project.travel.frontEnd.Screens.TripItinerary

object TravelApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun submitTripHistory(data: CitySearchViewModel.FeedbackData): Boolean {
        val request = TripHistoryRequest(
            userId = "currentUserId", // Replace with actual user ID (e.g., from Firebase Auth)
            citiesVisited = data.citiesVisited,
            activitiesDone = data.activitiesDone,
            hotelsStayed = data.hotelsStayed,
            ratings = data.rating,
            budgetRange = data.budgetRange,
            travelStyle = data.travelStyle,
            tripEndDate = java.time.LocalDate.now().toString(), // Use actual trip end date
            feedback = data.feedback.takeIf { it.isNotEmpty() }
        )
        return client.post("$BASE_URL/api/trip-history") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body() // Assume response is Boolean for success
    }

    suspend fun getCities(query: String): List<DestinationCity> {
        return client.get("$BASE_URL/api/destinations?name=$query").body()
    }

    suspend fun getCityDetails(cityId: String): CityDetailsResponse {
        return client.get("$BASE_URL/api/destinations/$cityId/details").body()
    }

    suspend fun getCityDetailsByName(cityName: String): CityDetailsResponse {
        return client.get("$BASE_URL/api/city-details/by-name") {
            parameter("name", cityName)
        }.body()
    }

    suspend fun getWikipediaSummary(city: String): WikipediaResponse {
        return client.get("https://en.wikipedia.org/w/api.php") {
            parameter("action", "query")
            parameter("titles", city)
            parameter("prop", "extracts")
            parameter("format", "json")
            parameter("exintro", true)
            parameter("origin", "*") // CORS workaround
        }.body()
    }

    suspend fun getCityPhotos(city: String): UnsplashResponse {
        return client.get("https://api.unsplash.com/search/photos") {
            parameter("query", city)
            parameter("client_id", "NEHMVuobdQVmCfXiH4XfIp-K7rY0965QnPKn7tS602A")
        }.body()
    }

    suspend fun getMyTrips(userId: String): List<TripItinerary> {
        return client.get("$BASE_URL/api/my-trips") {
            parameter("userId", userId)
        }.body()
    }
}