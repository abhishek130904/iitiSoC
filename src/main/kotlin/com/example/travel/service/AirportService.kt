package com.example.travel.network

import com.example.travel.dto.CityDTO
import com.example.travel.repository.AirportRepository
import org.springframework.stereotype.Service

@Service
class AirportService(private val airportRepository: AirportRepository) {

    fun getCitiesWithAirports(): List<CityDTO> {
        val airports = airportRepository.findAll() // fetch all airports from DB
        return airports.map { airport ->
            CityDTO(
                iataCode = airport.iataCode,
                city = airport.city,
                country = airport.country,
                airportName = airport.name
            )
        }
    }
}

