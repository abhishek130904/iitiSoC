package org.example.project.travel.frontEnd.pdf

import org.example.project.travel.frontend.navigation.Screen

data class TripSummary(
    val destination: String,
    val dates: String,
    val flightDetails: String,
    val hotelDetails: String,
    val activities: String,
    val meals: String,
    val costBreakdown: String,
    val notes: String?
)

expect fun generateTripSummaryPdf(trip: TripSummary): ByteArray