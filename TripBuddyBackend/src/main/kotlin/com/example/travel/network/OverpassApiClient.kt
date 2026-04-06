package com.example.travel.network

import com.example.travel.dto.DiningDTO
import com.example.travel.dto.ExperienceDTO
import com.example.travel.dto.LocationDTO
import com.example.travel.model.OverpassResponse
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.springframework.stereotype.Service

@Service
class OverpassApiClient(
    private val restTemplate: RestTemplate
) {
    private val baseUrl = "https://overpass-api.de/api/interpreter"

    private val cityCoordinates = mapOf(
        "Paris" to Pair(48.8566, 2.3522),
        "Tokyo" to Pair(35.6762, 139.6503)
    )

    fun getDiningByCity(city: String): List<DiningDTO> {
        val (lat, lon) = cityCoordinates[city] ?: throw IllegalArgumentException("City not supported: $city")

        val diningCategories = mapOf(
            "FINE_DINING" to "[amenity=restaurant][cuisine~'(fine_dining|french|gourmet)']",
            "VEGETARIAN_FRIENDLY" to "[amenity=restaurant][cuisine~'(vegetarian|vegan)']",
            "LOCAL_SPECIALS" to "[amenity=restaurant][cuisine~'(local|street_food|traditional)']"
        )

        val allDining = mutableListOf<DiningDTO>()
        diningCategories.forEach { (category, queryFilter) ->
            val query = """
                [out:json];
                node$queryFilter(around:10000,$lat,$lon);
                out body;
            """.trimIndent()

            val headers = HttpHeaders().apply {
                contentType = MediaType.TEXT_PLAIN
            }
            val entity = HttpEntity(query, headers)
            val response = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                entity,
                String::class.java
            ).body ?: throw RuntimeException("Failed to fetch dining data for $city")

            val overpassResponse: OverpassResponse = Json { ignoreUnknownKeys = true }.decodeFromString(response)

            val diningItems = overpassResponse.elements.map { element ->
                DiningDTO(
                    name = element.tags["name"] ?: "Unnamed Restaurant",
                    category = category,
                    description = element.tags["description"] ?: "No description available",
                    rating = element.tags["rating"]?.toDoubleOrNull() ?: 0.0,
                    price = element.tags["price"]?.toDoubleOrNull(),
                    url = element.tags["website"] ?: "",
                    city = city,
                    location = LocationDTO(
                        latitude = element.lat,
                        longitude = element.lon
                    )
                )
            }
            allDining.addAll(diningItems)
        }
        return allDining
    }

    fun getExperiencesByCity(city: String): List<ExperienceDTO> {
        val (lat, lon) = cityCoordinates[city] ?: throw IllegalArgumentException("City not supported: $city")

        val experienceCategories = mapOf(
            "TOURS" to "[tourism~'(attraction|museum|landmark)']",
            "CLASSES" to "[amenity~'(school|community_centre)'][access=customers]",
            "DAY_TRIPS" to "[tourism~'(viewpoint|park)'][distance~'day_trip']"
        )

        val allExperiences = mutableListOf<ExperienceDTO>()
        experienceCategories.forEach { (category, queryFilter) ->
            val query = """
                [out:json];
                node$queryFilter(around:10000,$lat,$lon);
                out body;
            """.trimIndent()

            val headers = HttpHeaders().apply {
                contentType = MediaType.TEXT_PLAIN
            }
            val entity = HttpEntity(query, headers)
            val response = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                entity,
                String::class.java
            ).body ?: throw RuntimeException("Failed to fetch experiences data for $city")

            val overpassResponse: OverpassResponse = Json { ignoreUnknownKeys = true }.decodeFromString(response)

            val experienceItems = overpassResponse.elements.map { element ->
                ExperienceDTO(
                    name = element.tags["name"] ?: "Unnamed Experience",
                    category = category,
                    description = element.tags["description"] ?: "No description available",
                    duration = element.tags["duration"] ?: "N/A",
                    price = element.tags["price"]?.toDoubleOrNull(),
                    url = element.tags["website"] ?: "",
                    city = city,
                    location = LocationDTO(
                        latitude = element.lat,
                        longitude = element.lon
                    )
                )
            }
            allExperiences.addAll(experienceItems)
        }
        return allExperiences
    }
}