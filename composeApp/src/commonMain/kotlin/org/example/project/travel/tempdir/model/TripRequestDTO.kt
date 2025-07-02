package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable
import org.example.project.travel.frontEnd.model.Meal

@Serializable
data class TripRequestDTO(
    val flightId: String,
    val cityName: String,
    val hotelName: String,
    val activities: List<TripActivity>,
    val meals: List<Meal>,
    val notes: String?
)