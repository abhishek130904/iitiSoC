package com.example.travel.model.amadeus

import java.time.LocalDateTime

data class AmadeusFlightResponse(
    val data: List<AmadeusFlightOffer>
)

data class AmadeusFlightOffer(
    val itineraries: List<Itinerary>,
    val price: Price
)

data class Itinerary(
    val duration: String?,
    val segments: List<Segment>
)

data class Segment(
    val departure: AmadeusFlightPoint,
    val arrival: AmadeusFlightPoint,
    val carrierCode: String,
    val number: String
)

data class AmadeusFlightPoint(
    val iataCode: String,
    val terminal: String?,
    val at: LocalDateTime?
)

data class Price(
    val currency: String,
    val total: String
)
