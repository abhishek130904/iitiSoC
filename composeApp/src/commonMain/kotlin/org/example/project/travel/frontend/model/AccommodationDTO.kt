package com.example.travel.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccommodationDTO(
    val id: String, // Changed to String to match JSON
    val name: String, // Matches JSON 'name'
    val rating: Double, // Matches JSON 'rating'
    val pricePerNight: Double, // Matches JSON 'pricePerNight'
    val currency: String, // Matches JSON 'currency'
    val amenities: List<String>, // Matches JSON 'amenities' array
    val latitude: Double, // Matches JSON 'latitude'
    val longitude: Double, // Matches JSON 'longitude'
    val bookingUrl: String, // Matches JSON 'bookingUrl'
    val airbnbUrl: String // Matches JSON 'airbnbUrl'
)