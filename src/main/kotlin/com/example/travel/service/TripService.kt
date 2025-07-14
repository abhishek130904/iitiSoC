package com.example.travel.service

import com.example.travel.model.ActivityEntity
import com.example.travel.model.MealEntity
import com.example.travel.model.TripEntity
import com.example.travel.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class TripService(
    private val tripRepository: TripRepository
) {
    fun saveTrip(userId: String, cityName: String, flightId: String, hotelName: String, activities: List<ActivityEntity>, meals: List<MealEntity>, notes: String?): Long {
        if (userId.isBlank() || cityName.isBlank() || hotelName.isBlank()) {
            throw IllegalArgumentException("Invalid trip data")
        }
        val trip = TripEntity(
            userId = userId,
            cityName = cityName,
            flightId = flightId,
            hotelName = hotelName,
            activities = activities,
            meals = meals,
            notes = notes
        )
        return tripRepository.save(trip).id
    }

    fun getTripsByUserId(userId: String): List<TripEntity> =
        tripRepository.findByUserId(userId)

    fun getTripsByUserIdOrderByCreatedAtDesc(userId: String): List<TripEntity> =
        tripRepository.findByUserIdOrderByCreatedAtDesc(userId)
}