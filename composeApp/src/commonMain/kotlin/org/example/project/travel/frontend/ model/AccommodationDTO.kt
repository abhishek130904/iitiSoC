package com.example.travel.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccommodationDTO(
    val id: String,
    val name: String,
    val rating: Float,
    val pricePerNight: Double,
    val currency: String,
    val amenities: List<String>,
    val latitude: Double,
    val longitude: Double,
    val bookingUrl: String,
    val airbnbUrl: String
)