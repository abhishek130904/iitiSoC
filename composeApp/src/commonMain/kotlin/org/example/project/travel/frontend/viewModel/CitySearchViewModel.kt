package org.example.project.travel.frontEnd.viewModel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontend.network.TravelApi

open class CitySearchViewModel<T : DestinationCity> : ViewModel() {
    private val _cities = MutableStateFlow<List<T>>(emptyList())
    open val cities: StateFlow<List<T>> = _cities

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error

    // New state for feedback data
    private val _feedbackData = MutableStateFlow<FeedbackData?>(null)
    val feedbackData: StateFlow<FeedbackData?> = _feedbackData.asStateFlow()

    data class FeedbackData(
        val citiesVisited: String = "",
        val activitiesDone: String = "",
        val hotelsStayed: String = "",
        val rating: Float = 0f,
        val budgetRange: String = "",
        val travelStyle: List<String> = emptyList(),
        val feedback: String = ""
    )

    fun searchCities(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val cities = TravelApi.getCities(query) as List<T>
                _cities.value = cities
            } catch (e: Exception) {
                _error.value = "Error fetching cities: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitFeedback(
        cities: String,
        activities: String,
        hotels: String,
        rating: Float,
        budget: String,
        travelStyle: List<String>,
        feedback: String
    ) {
        val data = FeedbackData(cities, activities, hotels, rating, budget, travelStyle, feedback)
        _feedbackData.value = data
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Simulate API call to save feedback (replace with real TravelApi endpoint)
                val response = TravelApi.submitTripHistory(data) // Assume this returns a success indicator
                if (response) {
                    _feedbackData.value = null // Clear after success
                } else {
                    _error.value = "Failed to submit feedback"
                }
            } catch (e: Exception) {
                _error.value = "Error submitting feedback: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Function to check trip end (to be triggered externally)
    fun checkTripEnd(tripEndDate: String) {
        viewModelScope.launch {
            val currentDate = java.time.LocalDate.now().toString()
            if (currentDate >= tripEndDate) {
                // Trigger navigation to feedback screen (to be handled by navigation logic)
                println("Trip ended on $tripEndDate, show feedback form")
            }
        }
    }
}