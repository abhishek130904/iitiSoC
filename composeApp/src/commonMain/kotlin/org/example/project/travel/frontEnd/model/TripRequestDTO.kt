package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable
import org.example.project.travel.frontEnd.model.Meal

@Serializable
data class TripCostBreakdown(
    val flight: Int,
    val hotel: Int,
    val activities: Int,
    val meals: Int,
    val transportation: Int
)

@Serializable
data class TripRequestDTO(
    val flightId: String,
    val cityName: String,
    val hotelName: String,
    val activities: List<TripActivity>,
    val meals: List<Meal>,
    val notes: String?,
    val costBreakdown: TripCostBreakdown,
    val hotelPrice: Int = 0,
    val flightPrice: Int = 0,
    val transportPrice: Int = 0
)