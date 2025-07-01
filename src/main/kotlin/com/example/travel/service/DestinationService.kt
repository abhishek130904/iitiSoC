package com.example.travel.service

import com.example.travel.dto.DestinationCity
import com.example.travel.network.OpenTripMapService
import com.example.travel.repository.DestinationCityRepository
import org.springframework.stereotype.Service

@Service
class DestinationCityService(
    private val repository: DestinationCityRepository,
    private val openTripMapService: OpenTripMapService
) {
    fun searchCitiesByName(name: String): List<DestinationCity> {
        return repository.findByCityContainingIgnoreCase(name)
    }

    fun getCityDetailsWithActivities(cityId: Long): DestinationCity? {
        return repository.findById(cityId).orElse(null)
        // Activities will be fetched in the controller using OpenTripMapService
    }
}