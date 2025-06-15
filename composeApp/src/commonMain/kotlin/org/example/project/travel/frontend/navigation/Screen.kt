package org.example.project.travel.frontend.navigation

import com.example.travel.model.dto.FlightDTO
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object FlightSearch : Screen()

    @Serializable
    data class FlightDetail(val flights: List<FlightDTO>) : Screen()

    @Serializable
    data class Hotel(val flightPrice: Double, val flightCurrency : String) : Screen()
}