package com.example.travel.dto

import kotlinx.serialization.Serializable

@Serializable
data class DiningDTO(
    val name: String,
    val category: String, // FINE_DINING, VEGETARIAN_FRIENDLY, LOCAL_SPECIALS
    val description: String,
    val rating: Double,
    val price: Double?,
    val url: String,
    val city: String,
    val location: LocationDTO? = null
)

@Serializable
data class LocationDTO(
    val latitude: Double,
    val longitude: Double
)