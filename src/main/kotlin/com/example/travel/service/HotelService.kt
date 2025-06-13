package com.example.travel.service

import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.network.HotelApiClient
import org.springframework.stereotype.Service

@Service
class HotelService(private val hotelApiClient: HotelApiClient) {

    fun searchHotelsByCity(city: String, sortBy: String, order: String, minRating: Float?): List<AccommodationDTO> {
        val hotels = hotelApiClient.searchHotelsByCity(city)
        var filteredHotels = hotels
            .mapIndexed { index, hotel ->
                AccommodationDTO(
                    id = hotel.id.toString(),
                    name = hotel.name,
                    rating = hotel.rating,
                    pricePerNight = hotel.pricePerNight,
                    currency = hotel.currency,
                    amenities = hotel.amenities?.split(",")?.map { it.trim() } ?: listOf("Wi-Fi"),
                    latitude = hotel.latitude,
                    longitude = hotel.longitude,
                    bookingUrl = "",
                    airbnbUrl = "https://www.airbnb.com/rooms/$index"
                )
            }

        // Apply filtering
        if (minRating != null) {
            filteredHotels = filteredHotels.filter { it.rating >= minRating }
        }

        // Apply sorting
        return when (sortBy.lowercase()) {
            "price" -> if (order.lowercase() == "asc") {
                filteredHotels.sortedBy { it.pricePerNight }
            } else {
                filteredHotels.sortedByDescending { it.pricePerNight }
            }
            "rating" -> if (order.lowercase() == "asc") {
                filteredHotels.sortedBy { it.rating }
            } else {
                filteredHotels.sortedByDescending { it.rating }
            }
            else -> filteredHotels
        }
    }
}