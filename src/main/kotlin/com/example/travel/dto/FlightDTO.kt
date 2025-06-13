package com.example.travel.dto

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class FlightDTO(
    val flightNumber: String,
    val airlineCode: String,
    val departure: FlightPointDTO,
    val arrival: FlightPointDTO,
    val duration: String,
    val price: Double,
    val currency: String
)

data class FlightPointDTO(
    val iataCode: String,
    val time: LocalDateTime?,
    val terminal: String?
)