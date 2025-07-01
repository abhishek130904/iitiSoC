package com.example.travel.service

import com.example.travel.dto.DiningDTO
import com.example.travel.repository.OverpassRepository
import org.springframework.stereotype.Service

@Service
class DiningService(
    private val repository: OverpassRepository
) {
    fun getDiningByCity(city: String): List<DiningDTO> {
        val dining = repository.getDiningByCity(city)
        if (dining.isEmpty()) {
            throw NoSuchElementException("No dining options found for $city")
        }
        return dining
    }
}