package org.example.project.travel.frontEnd.navigation

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
    data class Hotel(val selectedFlight: FlightDTO) : Screen()

    @Serializable
    data class TripItinerary(
        val selectedFlight: FlightDTO,
        val selectedHotel: com.example.travel.model.dto.AccommodationDTO,
        val selectedCityName: String
    ) : Screen()

    @Serializable
    data class CityDetails(val cityId: String, val cityName: String) : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    @Serializable
    data class TripConfirmation(val tripId: String) : Screen()
}