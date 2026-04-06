package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<WeatherDescription>,
    val main: WeatherMain,
    val name: String
)

@Serializable
data class WeatherDescription(
    val description: String,
    val icon: String
)

@Serializable
data class WeatherMain(
    val temp: Double
) 