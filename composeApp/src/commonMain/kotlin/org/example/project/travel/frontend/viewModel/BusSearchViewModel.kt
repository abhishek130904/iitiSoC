package org.example.project.travel.frontend.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BusSearchState(
    val fromCity: String = "BLR",
    val fromCityName: String = "Bangalore",
    val fromBusStation: String = "Majestic Bus Stand",
    val toCity: String = "MYS",
    val toCityName: String = "Mysore",
    val toBusStation: String = "Central Bus Stand",
    val selectedDate: String = "",
    val passengerCount: Int = 1,
    val busType: BusType = BusType.AC_SEATER
)

enum class BusType {
    AC_SEATER,
    AC_SLEEPER,
    NON_AC_SEATER,
    NON_AC_SLEEPER;

    fun displayName(): String = when (this) {
        AC_SEATER -> "AC Seater"
        AC_SLEEPER -> "AC Sleeper"
        NON_AC_SEATER -> "Non-AC Seater"
        NON_AC_SLEEPER -> "Non-AC Sleeper"
    }
}

class BusSearchViewModel {
    private val _state = MutableStateFlow(BusSearchState())
    val state: StateFlow<BusSearchState> = _state

    // Popular bus stations list
    val popularStations = listOf(
        Triple("BLR", "Bangalore", "Majestic Bus Stand"),
        Triple("MYS", "Mysore", "Central Bus Stand"),
        Triple("HYD", "Hyderabad", "Miyapur Bus Stand"),
        Triple("CHN", "Chennai", "CMBT"),
        Triple("CBE", "Coimbatore", "Gandhipuram"),
        Triple("TVM", "Thiruvananthapuram", "Central Bus Station"),
        Triple("MDU", "Madurai", "Mattuthavani"),
        Triple("VIZ", "Vizag", "RTC Complex")
    )

    fun updateFromCity(code: String, name: String, station: String) {
        _state.value = _state.value.copy(
            fromCity = code,
            fromCityName = name,
            fromBusStation = station
        )
    }

    fun updateToCity(code: String, name: String, station: String) {
        _state.value = _state.value.copy(
            toCity = code,
            toCityName = name,
            toBusStation = station
        )
    }

    fun updateDate(date: String) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun updateBusType(busType: BusType) {
        _state.value = _state.value.copy(busType = busType)
    }

    fun updatePassengerCount(count: Int) {
        _state.value = _state.value.copy(passengerCount = count)
    }

    fun swapCities() {
        val currentState = _state.value
        _state.value = currentState.copy(
            fromCity = currentState.toCity,
            fromCityName = currentState.toCityName,
            fromBusStation = currentState.toBusStation,
            toCity = currentState.fromCity,
            toCityName = currentState.fromCityName,
            toBusStation = currentState.fromBusStation
        )
    }
} 