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
    data class TripConfirmation(
        val destination: String,
        val dates: String,
        val flightDetails: String,
        val hotelDetails: String,
        val activities: String,
        val meals: String,
        val costBreakdown: String,
        val notes: String?
    ) : Screen()

    @Serializable
    data class StateScreen(val stateName: String) : Screen()

    @Serializable
    data class CategoryDetails(
        val categoryTitle: String,
        val categoryDescription: String,
        val destinations: List<String>
    ) : Screen()

    @Serializable
    data class MyTrips(val userId: String) : Screen()

    @Serializable
    data object TrainSearch : Screen()

    @Serializable
    data class TrainDetails(val fromStation: String, val toStation: String) : Screen()
}