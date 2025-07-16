package org.example.project.travel.frontEnd.network

import com.example.travel.network.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.travel.frontEnd.model.RecommendationResponse
import org.example.project.travel.frontEnd.model.Recommendations

class RecommendationApi(private val baseUrl: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getRecommendations(userId: String): Recommendations? {
        val url = "http://10.176.172.173:5000/api/recommendations/$userId"
        val response: RecommendationResponse = client.get(url).body()
        return if (response.success) response.recommendations else null
    }
}