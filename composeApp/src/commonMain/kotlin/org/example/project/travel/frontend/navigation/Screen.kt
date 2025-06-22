package org.example.project.travel.frontend.navigation

import com.example.travel.model.dto.FlightDTO
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object  Signup : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object  HomeScreen : Screen()

    @Serializable
    data object CitySearchScreen : Screen()

    @Serializable
    object FlightSearch : Screen()

    @Serializable
    data class FlightDetail(val flights: List<FlightDTO>) : Screen()

    @Serializable
    data class Hotel(val flightPrice: Double, val flightCurrency : String) : Screen()

    @Serializable
    data class CityDetails(val cityId: Long) : Screen()
}