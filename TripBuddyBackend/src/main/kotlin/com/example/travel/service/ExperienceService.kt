package com.example.travel.service

import com.example.travel.dto.ExperienceDTO
import com.example.travel.repository.OverpassRepository
import org.springframework.stereotype.Service

@Service
class ExperienceService(
    private val repository: OverpassRepository
) {
    fun getExperiencesByCity(city: String): List<ExperienceDTO> {
        val experiences = repository.getExperiencesByCity(city)
        if (experiences.isEmpty()) {
            throw NoSuchElementException("No experiences found for $city")
        }
        return experiences
    }
}