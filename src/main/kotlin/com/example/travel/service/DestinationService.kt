package com.example.travel.service

import com.example.travel.dto.CityDTO
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

    fun findBestMatchCity(city: String, state: String): DestinationCity? {
        val allCities = repository.findAll()
        return allCities.minByOrNull {
            levenshtein(it.city.lowercase(), city.lowercase()) + levenshtein(it.state.lowercase(), state.lowercase())
        }
    }

    // New method to find city by name only
    fun findCityByName(cityName: String): DestinationCity? {
        return repository.findByCityIgnoreCase(cityName)
    }

    // Simple Levenshtein distance implementation
    private fun levenshtein(lhs: String, rhs: String): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length
        var cost = Array(lhsLength + 1) { it }
        var newCost = Array(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i
            for (j in 1..lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[lhsLength]
    }
}