package com.example.travel.repository

import com.example.travel.dto.FlightDTO

interface FlightRepository {
    suspend fun searchFlights(
        fromCity: String,
        toCity: String,
        date: String,
        adults: Int,
        children: Int,
        infants: Int,
        cabinClass: String
    ): List<FlightDTO>
}