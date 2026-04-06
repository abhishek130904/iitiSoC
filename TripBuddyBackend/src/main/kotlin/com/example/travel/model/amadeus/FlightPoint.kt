package com.example.travel.model.amadeus

import java.time.LocalDateTime

data class FlightPoint(
    val iataCode: String,
    val time: LocalDateTime?
)