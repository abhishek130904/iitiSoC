package com.example.travel.viewmodel

//import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class FlightSearchViewModel : ViewModel() {

    data class FlightSearchState(
        val fromCity: String = "DEL",
        val fromCityName: String = "Delhi",
        val fromAirportName: String = "Indira Gandhi International Airport",
        val toCity: String = "BOM",
        val toCityName: String = "Mumbai",
        val toAirportName: String = "Chhatrapati Shivaji International Airport",
        val selectedDate: String = "",
        val adultCount: Int = 1,
        val childCount: Int = 0,
        val infantCount: Int = 0,
        val cabinClass: CabinClass = CabinClass.ECONOMY
    )

    enum class CabinClass {
        ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST;

        fun displayName(): String = when (this) {
            ECONOMY -> "Economy"
            PREMIUM_ECONOMY -> "Premium Economy"
            BUSINESS -> "Business"
            FIRST -> "First"
        }
    }

    private val _state = MutableStateFlow(FlightSearchState())
    val state: StateFlow<FlightSearchState> = _state.asStateFlow()

    val popularAirports = listOf(
        Triple("DEL", "Delhi", "Indira Gandhi International Airport"),
        Triple("BOM", "Mumbai", "Chhatrapati Shivaji International Airport"),
        Triple("BLR", "Bangalore", "Kempegowda International Airport"),
        Triple("HYD", "Hyderabad", "Rajiv Gandhi International Airport")
    )

    fun updateFromCity(code: String, name: String, airportName: String) {
        _state.value = _state.value.copy(
            fromCity = code,
            fromCityName = name,
            fromAirportName = airportName
        )
    }

    fun updateToCity(code: String, name: String, airportName: String) {
        _state.value = _state.value.copy(
            toCity = code,
            toCityName = name,
            toAirportName = airportName
        )
    }

    fun swapCities() {
        _state.value = _state.value.copy(
            fromCity = _state.value.toCity,
            fromCityName = _state.value.toCityName,
            fromAirportName = _state.value.toAirportName,
            toCity = _state.value.fromCity,
            toCityName = _state.value.fromCityName,
            toAirportName = _state.value.fromAirportName
        )
    }

    fun updateDate(date: String) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun updatePassengerCounts(adults: Int, children: Int, infants: Int) {
        _state.value = _state.value.copy(
            adultCount = adults,
            childCount = children,
            infantCount = infants
        )
    }

    fun updateCabinClass(cabinClass: CabinClass) {
        _state.value = _state.value.copy(cabinClass = cabinClass)
    }

}