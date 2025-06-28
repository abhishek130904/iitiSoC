package org.example.project.travel.frontend.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.travel.frontend.model.*

object TravelApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getCities(query: String): List<DestinationCity> {
        return client.get("http://192.168.213.173:8080/api/destinations?name=$query").body()
    }

    suspend fun getCityDetails(cityId: String): CityDetailsResponse {
        return client.get("http://192.168.213.173:8080/api/destinations/$cityId/details").body()
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
}