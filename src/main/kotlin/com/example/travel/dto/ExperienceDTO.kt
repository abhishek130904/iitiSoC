package com.example.travel.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceDTO(
    val name: String,
    val category: String, // TOURS, CLASSES, DAY_TRIPS
    val description: String,
    val duration: String,
    val price: Double?,
    val url: String,
    val city: String,
    val location: LocationDTO? = null
)