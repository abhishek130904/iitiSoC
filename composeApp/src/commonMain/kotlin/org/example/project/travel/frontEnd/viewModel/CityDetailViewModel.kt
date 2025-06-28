package org.example.project.travel.frontEnd.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.travel.frontend.model.CityDetailsResponse
import org.example.project.travel.frontend.model.UnsplashResponse
import org.example.project.travel.frontend.model.WikipediaResponse
import org.example.project.travel.frontend.network.TravelApi

class CityDetailsViewModel(private val cityId: String) : ViewModel() {
    private val _cityDetails = MutableStateFlow<CityDetailsResponse?>(null)
    val cityDetails: StateFlow<CityDetailsResponse?> = _cityDetails

    private val _wikipediaData = MutableStateFlow<WikipediaResponse?>(null)
    val wikipediaData: StateFlow<WikipediaResponse?> = _wikipediaData

    private val _unsplashPhotos = MutableStateFlow<UnsplashResponse?>(null)
    val unsplashPhotos: StateFlow<UnsplashResponse?> = _unsplashPhotos

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchCityDetails()
    }

    fun fetchCityDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _cityDetails.value = TravelApi.getCityDetails(cityId)
                _cityDetails.value?.let {
                    _wikipediaData.value = TravelApi.getWikipediaSummary(it.city)
                    _unsplashPhotos.value = TravelApi.getCityPhotos(it.city)
                }
            } catch (e: Exception) {
                _error.value = "Error fetching details: ${e.message}"
                println("Error fetching details: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}