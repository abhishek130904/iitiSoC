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
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HotelViewModel(private val apiClient: HotelApiClient, private val city: String) : ViewModel() {
    private val _hotels = MutableStateFlow<List<AccommodationDTO>>(emptyList())
    val hotels = _hotels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        fetchHotels(city)
    }

    private fun fetchHotels(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val hotels = apiClient.getHotels(city)
                _hotels.value = hotels
            } catch (e: Exception) {
                _error.value = "Failed to load hotels: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}