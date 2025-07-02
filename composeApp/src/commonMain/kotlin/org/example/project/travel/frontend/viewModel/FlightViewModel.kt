package com.example.travel.viewmodel

import com.example.travel.model.dto.FlightDTO
import com.example.travel.network.ApiService
import com.example.travel.dto.CityDTO
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

    // City mapping
    private val _cities = MutableStateFlow<List<CityDTO>>(emptyList())
    val cities: StateFlow<List<CityDTO>> = _cities.asStateFlow()
    private val iataToCityName = mutableMapOf<String, String>()

    init {
        // Set default search state
        _searchState.value = _searchState.value.copy(
            fromCity = "DEL",
            fromCityName = "Delhi",
            fromAirportName = "Indira Gandhi International Airport",
            toCity = "BOM",
            toCityName = "Mumbai",
            toAirportName = "Chhatrapati Shivaji International Airport",
            selectedDate = "2025-07-03"
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
        val formattedDate = formatDateForApi(date)
        println("FlightViewModel: Sending formatted date to API: $formattedDate")
        try {
            val today = java.time.LocalDate.now()
            val selected = java.time.LocalDate.parse(formattedDate)
            println("FlightViewModel: Today is $today, selected date is $selected")
            if (selected.isBefore(today)) {
                println("FlightViewModel: Blocked API call - selected date $selected is in the past compared to today $today")
                _flightsError.value = "Selected date is in the past. Please choose a valid date."
                _flights.value = emptyList()
                _isFlightsLoading.value = false
                return
            }
        } catch (e: Exception) {
            println("FlightViewModel: Exception during date validation: ${e.message}")
            _flightsError.value = "Invalid date format."
            _flights.value = emptyList()
            _isFlightsLoading.value = false
            return
        }
        viewModelScope.launch {
            _isFlightsLoading.value = true
            println("FlightViewModel: searchFlights - Loading started, isFlightsLoading=${_isFlightsLoading.value}")
            try {
                val response = apiService.searchFlights(
                    fromCity = fromCity,
                    toCity = toCity,
                    date = formattedDate,
                    adults = adults,
                    children = children,
                    infants = infants,
                    cabinClass = cabinClass
                )
                println("FlightViewModel: searchFlights - API response received")
                response
                    .onSuccess { flights ->
                        println("FlightViewModel: searchFlights success - flights count=${flights.size}, flights=$flights")
                        _flights.value = flights
                        _flightsError.value = null
                    }
                    .onFailure { exception ->
                        println("FlightViewModel: searchFlights failed - error=${exception.message}")
                        _flightsError.value = exception.message
                        _flights.value = emptyList()
                    }
            } catch (e: Exception) {
                println("FlightViewModel: searchFlights exception - error=${e.message}")
                _flightsError.value = e.message
                _flights.value = emptyList()
            } finally {
                println("FlightViewModel: searchFlights - Loading finished, isFlightsLoading=${_isFlightsLoading.value}, flights=${_flights.value}, error=${_flightsError.value}")
                _isFlightsLoading.value = false
            }
        }
    }

    private fun formatDateForApi(date: String): String {
        println("formatDateForApi: input date string = '$date'")
        // If already in yyyy-MM-dd format, return as is
        val isoRegex = Regex("""\d{4}-\d{2}-\d{2}""")
        if (isoRegex.matches(date)) {
            println("formatDateForApi: input is already in yyyy-MM-dd format, returning as is")
            return date
        }
        // Otherwise, try to parse "03 JUL 25" etc.
        val regex = Regex("""(\d{2})\s([A-Za-z]{3})\s(\d{2,4})""")
        val match = regex.find(date)
        if (match != null) {
            val (day, monthStr, yearStr) = match.destructured
            println("formatDateForApi: parsed day=$day, monthStr=$monthStr, yearStr=$yearStr")
            val month = when (monthStr.uppercase()) {
                "JAN" -> "01"
                "FEB" -> "02"
                "MAR" -> "03"
                "APR" -> "04"
                "MAY" -> "05"
                "JUN" -> "06"
                "JUL" -> "07"
                "AUG" -> "08"
                "SEP" -> "09"
                "OCT" -> "10"
                "NOV" -> "11"
                "DEC" -> "12"
                else -> "01"
            }
            val year = if (yearStr.length == 2) "20$yearStr" else yearStr
            val formatted = "$year-$month-$day"
            println("formatDateForApi: output formatted date = '$formatted'")
            return formatted
        }
        println("formatDateForApi: fallback date used, returning input")
        return date
    }


    fun clearFlights(flightViewModel: FlightViewModel) {
        flightViewModel._flights.value = emptyList()
        println("FlightViewModel: Flights cleared - flights=${_flights.value}")
        flightViewModel._flightsError.value = null
        flightViewModel._isFlightsLoading.value = false
        println("FlightViewModel: clearFlights called - flights=${flightViewModel._flights.value}, error=${flightViewModel._flightsError.value}, isLoading=${flightViewModel._isFlightsLoading.value}")
    }

    fun getCityNameByIata(iata: String): String? = iataToCityName[iata]

    fun fetchCities() {
        viewModelScope.launch {
            val result = apiService.getCitiesWithAirports()
            result.onSuccess { cityList ->
                _cities.value = cityList
                iataToCityName.clear()
                iataToCityName.putAll(cityList.associate { it.iataCode to it.city })
            }
            // Optionally handle errors
        }
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