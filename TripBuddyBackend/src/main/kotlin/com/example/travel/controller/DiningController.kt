package com.example.travel.controller

import com.example.travel.dto.DiningDTO
import com.example.travel.service.DiningService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class DiningController(
    private val diningService: DiningService
) {
    @GetMapping("/api/dining")
    fun getDining(@RequestParam("city") city: String?): List<DiningDTO> {
        if (city == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "City parameter is required")
        }
        return try {
            diningService.getDiningByCity(city)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message ?: "No dining options found for $city")
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch dining options: ${e.message}")
        }
    }
}