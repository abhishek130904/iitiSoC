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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontend.network.TravelApi

//class CitySearchViewModel<T> : ViewModel() {
//    private val client = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json {
//                ignoreUnknownKeys = true
//                isLenient = true
//            })
//        }
//    }
//
//    private val _cities = MutableStateFlow<List<T>>(emptyList())
//    val cities: StateFlow<List<T>> = _cities
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error
//
//    private var searchJob: Job? = null
//
//    init {
//        // viewModelScope.launch {
//        //     fetchCities()
//        // }
//    }
//
//    private suspend fun fetchCities(query: String = "") {
//        searchJob?.cancel()
//        searchJob = viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//            delay(300)
//            try {
//                @Suppress("UNCHECKED_CAST")
//                val cityList = client.get("http://192.168.212.251:8080/api/destinations") {
//                    if (query.isNotEmpty()) parameter("name", query) // Match controller param
//                }.body<List<DestinationCity>>() as List<T>
//                _cities.value = cityList
//            } catch (e: Exception) {
//                _error.value = "Failed to load cities: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun searchCities(query: String) {
//        viewModelScope.launch {
//            fetchCities(query)
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        client.close()
//    }
//}

open class CitySearchViewModel<T : DestinationCity> : ViewModel() {
    private val _cities = MutableStateFlow<List<T>>(emptyList())
    open val cities: StateFlow<List<T>> = _cities

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error

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
}