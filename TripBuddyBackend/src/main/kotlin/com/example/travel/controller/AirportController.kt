package com.example.travel.controller

import com.example.travel.dto.CityDTO
import com.example.travel.network.AirportService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/api/airports")
class AirportController(private val airportService: AirportService) {

    @GetMapping("/cities")
    fun getCitiesWithAirports(): List<CityDTO> {
        return airportService.getCitiesWithAirports()
    }
}
