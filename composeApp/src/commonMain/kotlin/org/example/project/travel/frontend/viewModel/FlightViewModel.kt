package com.example.travel.viewmodel

import com.example.travel.model.dto.FlightDTO
import com.example.travel.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class FlightViewModel : ViewModel() {

    private val apiService = ApiService()

    // Flight search state
    private val _searchState = MutableStateFlow(FlightSearchState())
    val searchState: StateFlow<FlightSearchState> = _searchState.asStateFlow()

    // Flight results state
    private val _flights = MutableStateFlow<List<FlightDTO>>(emptyList()).also {
        println("FlightViewModel: _flights initialized - flights=${it.value}")
    }
    val flights: StateFlow<List<FlightDTO>> = _flights.asStateFlow()

    private val _isFlightsLoading = MutableStateFlow(false)
    val isFlightsLoading: StateFlow<Boolean> = _isFlightsLoading.asStateFlow()

    private val _flightsError = MutableStateFlow<String?>(null)
    val flightsError: StateFlow<String?> = _flightsError.asStateFlow()

    init {
        // Set default search state
        _searchState.value = _searchState.value.copy(
            fromCity = "DEL",
            fromCityName = "Delhi",
            fromAirportName = "Indira Gandhi International Airport",
            toCity = "BOM",
            toCityName = "Mumbai",
            toAirportName = "Chhatrapati Shivaji Maharaj International Airport"
        )
        println("FlightViewModel: Initialized with default searchState - fromCity=${_searchState.value.fromCity}, toCity=${_searchState.value.toCity}, selectedDate=${_searchState.value.selectedDate}, adults=${_searchState.value.adultCount}, children=${_searchState.value.childCount}, infants=${_searchState.value.infantCount}, cabinClass=${_searchState.value.cabinClass}")
    }

    fun updateFromCity(code: String, city: String, airport: String) {
        _searchState.value = _searchState.value.copy(
            fromCity = code,
            fromCityName = city,
            fromAirportName = airport
        )
        println("FlightViewModel: Updated fromCity - code=$code, city=$city, airport=$airport")
    }

    fun updateToCity(code: String, city: String, airport: String) {
        _searchState.value = _searchState.value.copy(
            toCity = code,
            toCityName = city,
            toAirportName = airport
        )
        println("FlightViewModel: Updated toCity - code=$code, city=$city, airport=$airport")
    }

    fun swapCities() {
        _searchState.value = _searchState.value.copy(
            fromCity = _searchState.value.toCity,
            fromCityName = _searchState.value.toCityName,
            fromAirportName = _searchState.value.toAirportName,
            toCity = _searchState.value.fromCity,
            toCityName = _searchState.value.fromCityName,
            toAirportName = _searchState.value.fromAirportName
        )
        println("FlightViewModel: Swapped cities - new fromCity=${_searchState.value.fromCity}, toCity=${_searchState.value.toCity}")
    }

    fun updateDate(date: String) {
        _searchState.value = _searchState.value.copy(selectedDate = date)
        println("FlightViewModel: Updated date - date=$date")
    }

    fun updatePassengerCounts(adults: Int, children: Int, infants: Int) {
        _searchState.value = _searchState.value.copy(
            adultCount = adults,
            childCount = children,
            infantCount = infants
        )
        println("FlightViewModel: Updated passenger counts - adults=$adults, children=$children, infants=$infants")
    }

    fun updateCabinClass(cabinClass: CabinClass) {
        _searchState.value = _searchState.value.copy(cabinClass = cabinClass)
        println("FlightViewModel: Updated cabin class - cabinClass=$cabinClass")
    }

    fun searchFlights(
        fromCity: String,
        toCity: String,
        date: String,
        adults: Int,
        children: Int,
        infants: Int,
        cabinClass: String
    ) {
        println("FlightViewModel: searchFlights called - fromCity=$fromCity, toCity=$toCity, date=$date, adults=$adults, children=$children, infants=$infants, cabinClass=$cabinClass")
        viewModelScope.launch {
            _isFlightsLoading.value = true
            println("FlightViewModel: searchFlights - Loading started, isFlightsLoading=${_isFlightsLoading.value}")
            try {
                val response = apiService.searchFlights(
                    fromCity = fromCity,
                    toCity = toCity,
                    date = date,
                    adults = adults,
                    children = children,
                    infants = infants,
                    cabinClass = cabinClass
                )
                println("FlightViewModel: searchFlights - API response received")
                response
                    .onSuccess { flights ->
                        _flights.value = flights
                        _flightsError.value = null
                        println("FlightViewModel: searchFlights success - flights count=${flights.size}, flights=$flights")
                    }
                    .onFailure { exception ->
                        _flightsError.value = exception.message
                        _flights.value = emptyList()
                        println("FlightViewModel: searchFlights failed - error=${exception.message}")
                    }
            } catch (e: Exception) {
                _flightsError.value = e.message
                _flights.value = emptyList()
                println("FlightViewModel: searchFlights exception - error=${e.message}")
            } finally {
                _isFlightsLoading.value = false
                println("FlightViewModel: searchFlights - Loading finished, isFlightsLoading=${_isFlightsLoading.value}, flights=${_flights.value}, error=${_flightsError.value}")
            }
        }
    }

    fun clearFlights(flightViewModel: FlightViewModel) {
        flightViewModel._flights.value = emptyList()
        println("FlightViewModel: Clearing flights - caller=${Thread.currentThread().stackTrace[3]}")
        println("FlightViewModel: Flights cleared - flights=${_flights.value}")
        flightViewModel._flightsError.value = null
        flightViewModel._isFlightsLoading.value = false
        println("FlightViewModel: clearFlights called - flights=${flightViewModel._flights.value}, error=${flightViewModel._flightsError.value}, isLoading=${flightViewModel._isFlightsLoading.value}")
    }

    data class FlightSearchState(
        val fromCity: String = "",
        val fromCityName: String = "",
        val fromAirportName: String = "",
        val toCity: String = "",
        val toCityName: String = "",
        val toAirportName: String = "",
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
}