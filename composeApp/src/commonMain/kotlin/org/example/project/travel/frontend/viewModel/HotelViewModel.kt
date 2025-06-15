package org.example.project.travel.frontend.viewModel

import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.network.HotelApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotelViewModel(private val apiClient: HotelApiClient) {
    private val scope = CoroutineScope(Dispatchers.IO)

    // List of cities (hardcoded for now, can fetch dynamically later)
    private val cities = listOf(
        "Mumbai", "Delhi", "Goa", "Jaipur", "Kerala",
        "Shimla", "Darjeeling", "Bangalore", "Chennai", "Kolkata"
    )

    // State for UI
    private val _selectedCity = MutableStateFlow("Mumbai")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _hotels = MutableStateFlow<List<AccommodationDTO>>(emptyList())
    val hotels: StateFlow<List<AccommodationDTO>> = _hotels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchHotels("Mumbai") // Fetch hotels for default city
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
        fetchHotels(city)
    }

    fun getCities(): List<String> = cities

    private fun fetchHotels(city: String) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = apiClient.getHotelsByCity(city)
                _hotels.value = response
            } catch (e: Exception) {
                _error.value = "Failed to fetch hotels: ${e.message}"
                _hotels.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}