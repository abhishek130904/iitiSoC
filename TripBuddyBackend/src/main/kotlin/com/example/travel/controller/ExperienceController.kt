package com.example.travel.controller

import com.example.travel.dto.ExperienceDTO
import com.example.travel.service.ExperienceService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ExperienceController(
    private val experienceService: ExperienceService
) {
    @GetMapping("/api/experiences")
    fun getExperiences(@RequestParam("city") city: String?): List<ExperienceDTO> {
        if (city == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "City parameter is required")
        }
        return try {
            experienceService.getExperiencesByCity(city)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message ?: "No experiences found for $city")
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch experiences: ${e.message}")
        }
    }
}