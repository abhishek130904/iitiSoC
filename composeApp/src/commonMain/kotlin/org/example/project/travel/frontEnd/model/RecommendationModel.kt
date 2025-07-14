package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable

@Serializable
data class Recommendations(
    val generic_deals: List<String>,
    val generic_packing_tips: List<String>,
    val next_city_recommendation: String? = null,
    val other_hotels: List<String> = emptyList(),
    val similar_destinations: List<String> = emptyList()
)

@Serializable
data class RecommendationResponse(
    val recommendations: Recommendations,
    val success: Boolean
)