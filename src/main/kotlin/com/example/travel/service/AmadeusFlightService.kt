package com.example.travel.service

import com.example.travel.dto.FlightDTO
import com.example.travel.dto.FlightPointDTO
import com.example.travel.exception.FlightSearchException
import com.example.travel.model.amadeus.AmadeusFlightResponse
import com.example.travel.repository.FlightRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.time.LocalDateTime

@Service
class AmadeusFlightService(
    @Value("\${amadeus.api.key}") private val apiKey: String,
    @Value("\${amadeus.api.secret}") private val apiSecret: String
) : FlightRepository {

    private val baseUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers"
    private val tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token"
    private val restTemplate = RestTemplate()
    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    // In-memory cache
    private val cache = mutableMapOf<String, List<FlightDTO>>()

    override suspend fun searchFlights(
        fromCity: String,
        toCity: String,
        date: String,
        adults: Int,
        children: Int,
        infants: Int,
        cabinClass: String
    ): List<FlightDTO> {
        if (fromCity.isBlank() || toCity.isBlank()) {
            throw FlightSearchException(HttpStatus.BAD_REQUEST.value(), "Origin and destination cities cannot be empty")
        }
        if (adults < 1) {
            throw FlightSearchException(HttpStatus.BAD_REQUEST.value(), "At least one adult is required")
        }

        val cacheKey = "$fromCity-$toCity-$date-$adults-$children-$infants-$cabinClass"

        cache[cacheKey]?.let { return it }

        try {
            val token = getAccessToken()

            val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("originLocationCode", fromCity)
                .queryParam("destinationLocationCode", toCity)
                .queryParam("departureDate", date)
                .queryParam("adults", adults)
                .queryParam("children", children)
                .queryParam("infants", infants)
                .queryParam("travelClass", cabinClass.uppercase())
                .queryParam("currencyCode", "INR")
                .build()
                .toUriString()

            val headers = org.springframework.http.HttpHeaders().apply {
                set("Authorization", "Bearer $token")
            }
            val request = org.springframework.http.RequestEntity.get(url)
                .headers(headers)
                .build()

            val response = restTemplate.exchange<String>(request)
            val responseBody = response.body
                ?: throw FlightSearchException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Empty response from Amadeus API")

            val amadeusResponse = objectMapper.readValue(responseBody, AmadeusFlightResponse::class.java)

            if (amadeusResponse.data.isEmpty()) {
                throw FlightSearchException(HttpStatus.NOT_FOUND.value(), "No flights found for the given search criteria")
            }

            val flightDTOs = amadeusResponse.data.map { flight ->
                val itinerary = flight.itineraries.first()
                val segment = itinerary.segments.first()

                FlightDTO(
                    flightNumber = "${segment.carrierCode}${segment.number}",
                    airlineCode = segment.carrierCode,
                    departure = FlightPointDTO(
                        iataCode = segment.departure.iataCode,
                        time = segment.departure.at,
                        terminal = segment.departure.terminal
                    ),
                    arrival = FlightPointDTO(
                        iataCode = segment.arrival.iataCode,
                        time = segment.arrival.at,
                        terminal = segment.arrival.terminal
                    ),
                    duration = itinerary.duration ?: "N/A",
                    price = flight.price.total.toDouble(),
                    currency = flight.price.currency
                )
            }

            cache[cacheKey] = flightDTOs

            return flightDTOs
        } catch (e: HttpClientErrorException) {
            throw FlightSearchException(e.statusCode.value(), "Amadeus API error: ${e.message}")
        } catch (e: Exception) {
            throw FlightSearchException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch flights: ${e.message}")
        }
    }

    private fun getAccessToken(): String {
        try {
            val headers = org.springframework.http.HttpHeaders().apply {
                set("Content-Type", "application/x-www-form-urlencoded")
            }
            val body = "grant_type=client_credentials&client_id=$apiKey&client_secret=$apiSecret"
            val request = org.springframework.http.RequestEntity.post(tokenUrl)
                .headers(headers)
                .body(body)

            val response = restTemplate.exchange<Map<*, *>>(request)
            return response.body?.get("access_token") as? String
                ?: throw FlightSearchException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve access token")
        } catch (e: Exception) {
            throw FlightSearchException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Token retrieval failed: ${e.message}")
        }
    }
}
