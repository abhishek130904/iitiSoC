package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable

@Serializable
data class TripHistoryRequest(
    val userId: String,
    val citiesVisited: String,
    val activitiesDone: String,
    val hotelsStayed: String,
    val ratings: Float,
    val budgetRange: String,
    val travelStyle: List<String>,
    val tripEndDate: String,
    val feedback: String? = null
)