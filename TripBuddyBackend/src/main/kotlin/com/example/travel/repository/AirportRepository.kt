package com.example.travel.repository

import com.example.travel.model.Airport
import org.springframework.data.jpa.repository.JpaRepository

interface AirportRepository : JpaRepository<Airport, Long> {
    fun findByCountryOrderByCityAsc(country: String): List<Airport>
}