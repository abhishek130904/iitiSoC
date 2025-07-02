package org.example.project.travel.frontEnd.network

import com.example.travel.network.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.contentType
import org.example.project.travel.frontEnd.model.TripRequestDTO
import org.example.project.travel.frontEnd.model.TripActivity
import org.example.project.travel.frontEnd.model.Meal
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.getCurrentFirebaseUserUid

class TripService(private val authService: AuthService) {
    private val client = ApiClient.client

    suspend fun saveTrip(trip: TripRequestDTO): Long {
        val userId = getCurrentFirebaseUserUid() ?: throw Exception("User not authenticated")
        println("[TripService] Sending trip data: $trip for userId: $userId")
        try {
            val response = client.post("http://192.168.62.173:8080/api/trips") {
                headers {
                    append("X-User-Id", userId)
                    contentType(io.ktor.http.ContentType.Application.Json)
                    accept(io.ktor.http.ContentType.Application.Json) // ✅ Important
                }
                setBody(trip)
            }
            println("[TripService] Received response: ${response.status}")
            val tripResponse: TripResponseDTO = response.body()
            println("[TripService] Trip saved with id: ${tripResponse.id}")
            return tripResponse.id
        } catch (e: Exception) {
            println("[TripService] Error saving trip: ${e.message}")
            throw e
        }
    }
}

@kotlinx.serialization.Serializable
data class TripResponseDTO(val id: Long)
