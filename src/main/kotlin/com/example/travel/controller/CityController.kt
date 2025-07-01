package com.example.travel.controller

import com.example.travel.dto.DestinationCity
import com.example.travel.network.OpenTripMapService
import com.example.travel.service.DestinationCityService
import com.example.travel.service.GeocodingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/destinations")
class DestinationCityController(
    private val service: DestinationCityService,
    private val openTripMapService: OpenTripMapService,
    private val geocodingService: GeocodingService
) {
    data class CityDetailsResponse(
        val id: Long,
        val country: String,
        val city: String,
        val state: String,
        val cityCode: Long,
        val activities: List<OpenTripMapService.Feature>
    )

    @GetMapping
    fun getCities(@RequestParam(name = "name", required = false) name: String?): List<DestinationCity> {
        return if (name.isNullOrEmpty()) {
            service.searchCitiesByName("")
        } else {
            service.searchCitiesByName(name)
        }
    }

    @GetMapping("/{cityId}/details")
    suspend fun getCityDetails(@PathVariable cityId: Long): CityDetailsResponse? {
        val city = service.getCityDetailsWithActivities(cityId)
        if (city != null) {
            val (lat, lon) = geocodingService.getCoordinates(city.city) ?: (null to null)
            val activities = if (lat != null && lon != null) {
                openTripMapService.getActivitiesForCity(city.city, lat, lon)
            } else {
                emptyList()
            }
            return CityDetailsResponse(
                id = city.id,
                country = city.country,
                city = city.city,
                state = city.state,
                cityCode = city.cityCode,
                activities = activities
            )
        }
        return null
    }
}