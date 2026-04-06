package com.example.travel.controller


import com.example.travel.dto.FlightDTO
import com.example.travel.repository.FlightRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FlightController(
    private val flightRepository: FlightRepository
) {

    @GetMapping("/flights/search")
    suspend fun searchFlights(
        @RequestParam("fromCity") fromCity: String,
        @RequestParam("toCity") toCity: String,
        @RequestParam("date") date: String,
        @RequestParam("adults") adults: Int,
        @RequestParam("children") children: Int,
        @RequestParam("infants") infants: Int,
        @RequestParam("cabinClass") cabinClass: String
    ): List<FlightDTO> {
        return flightRepository.searchFlights(fromCity, toCity, date, adults, children, infants, cabinClass)
    }
}