package com.example.travel.repository

import com.example.travel.dto.DiningDTO
import com.example.travel.dto.ExperienceDTO
import com.example.travel.network.OverpassApiClient
import org.springframework.stereotype.Repository

@Repository
class OverpassRepository(
    private val apiClient: OverpassApiClient
) {
    fun getDiningByCity(city: String): List<DiningDTO> {
        return apiClient.getDiningByCity(city)
    }

    fun getExperiencesByCity(city: String): List<ExperienceDTO> {
        return apiClient.getExperiencesByCity(city)
    }
}