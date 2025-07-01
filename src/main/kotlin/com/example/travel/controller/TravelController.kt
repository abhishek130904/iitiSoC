package com.example.travel.controller

import com.example.travel.model.ActivityEntity
import com.example.travel.model.MealEntity
import com.example.travel.service.TripService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class TravelController(
    private val tripService: TripService
) {
    @PostMapping("/trips")
    fun saveTrip(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody tripRequest: TripRequestDTO
    ): ResponseEntity<TripResponseDTO> {
        return try {
            val tripId = tripService.saveTrip(
                userId,
                tripRequest.cityName,
                tripRequest.flightId,
                tripRequest.hotelName,
                tripRequest.activities,
                tripRequest.meals,
                tripRequest.notes
            )
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(TripResponseDTO(tripId))
        } catch (e: Exception) {
            ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .build()
        }
    }
}

data class TripRequestDTO(
    val cityName: String,
    val flightId: String,
    val hotelName: String,
    val activities: List<ActivityEntity>,
    val meals: List<MealEntity>,
    val notes: String?
)

data class TripResponseDTO(val id: Long)