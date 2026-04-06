package com.example.travel.viewmodel

import com.example.travel.dto.CityDTO
import com.example.travel.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirportCityViewModel : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    private val apiService = ApiService()

    // Cities state
    private val _cities = MutableStateFlow<List<CityDTO>>(emptyList())
    val cities: StateFlow<List<CityDTO>> = _cities

    private val _isCitiesLoading = MutableStateFlow(false)
    val isCitiesLoading: StateFlow<Boolean> = _isCitiesLoading

    private val _citiesError = MutableStateFlow<String?>(null)
    val citiesError: StateFlow<String?> = _citiesError

    // Popular airports (used for mapping IATA codes to airport names)
    val popularAirports = listOf(
        "DEL" to ("Delhi" to "Indira Gandhi International Airport"),
        "BOM" to ("Mumbai" to "Chhatrapati Shivaji Maharaj International Airport"),
        "BLR" to ("Bangalore" to "Kempegowda International Airport"),
        "MAA" to ("Chennai" to "Chennai International Airport")
    )

    init {
        fetchCities()
    }

    private fun fetchCities() {
        launch {
            _isCitiesLoading.value = true
            val result = apiService.getCitiesWithAirports()
            when {
                result.isSuccess -> {
                    val cityList = result.getOrDefault(emptyList())
                    if (cityList.isEmpty()) {
                        _citiesError.value = "No cities found. Tap to retry."
                    } else {
                        _cities.value = cityList
                    }
                }
                result.isFailure -> {
                    _citiesError.value = "Failed to load cities: ${result.exceptionOrNull()?.message}. Tap to retry."
                }
            }
            _isCitiesLoading.value = false
        }
    }

    fun retryFetchCities() {
        _citiesError.value = null
        fetchCities()
    }
}