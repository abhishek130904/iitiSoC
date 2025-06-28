package com.example.travel.dto

import kotlinx.serialization.Serializable

@Serializable
data class CityDTO(
    val iataCode: String,
    val city: String,
    val country: String,
    val airportName: String
)