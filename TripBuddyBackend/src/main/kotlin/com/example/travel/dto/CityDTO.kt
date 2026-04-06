// src/main/kotlin/com/example/travel/dto/CityDTO.kt
package com.example.travel.dto

import kotlinx.serialization.Serializable

@Serializable
data class CityDTO(
    val iataCode: String,
    val city: String,
    val country: String,
    val airportName: String
)
