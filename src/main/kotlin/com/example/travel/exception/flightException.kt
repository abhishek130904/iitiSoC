package com.example.travel.exception

data class FlightSearchException(
    val status: Int,
    val errorMessage: String
) : RuntimeException(errorMessage)