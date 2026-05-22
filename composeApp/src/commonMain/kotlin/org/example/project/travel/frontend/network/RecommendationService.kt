package org.example.project.travel.frontEnd.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.travel.frontEnd.model.RecommendationResponse
import org.example.project.travel.frontEnd.model.Recommendations
import org.example.project.travel.frontend.config.ApiConfig

class RecommendationApi(private val baseUrl: String = ApiConfig.recommendationBaseUrl) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    /**
     * Fetches personalized recommendations for a user.
     *
     * IMPORTANT: [baseUrl] must be configured in local.properties before release:
     *   recommendation.base.url=https://your-production-url.com
     *
     * The hardcoded internal IP (10.17.2.32) has been removed for security.
     */
    suspend fun getRecommendations(userId: String): Recommendations? {
        if (baseUrl.isBlank()) {
            println("RecommendationApi: base URL not configured — skipping recommendation fetch")
            return null
        }
        val url = "$baseUrl/api/recommendations/$userId"
        val response: RecommendationResponse = client.get(url).body()
        return if (response.success) response.recommendations else null
    }
}