package com.example.travel.repository

import com.example.travel.dto.DestinationCity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DestinationCityRepository : JpaRepository<DestinationCity, Long> {
    fun findByCityContainingIgnoreCase(city: String): List<DestinationCity>
    
    // New method to find city by exact name (case-insensitive)
    fun findByCityIgnoreCase(city: String): DestinationCity?
}