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
                    name = hotel.hotelName,
                    rating = hotel.hotelRating.toFloat(),
                    pricePerNight = hotel.hotelPrice,
                    currency = "INR", // Default value
                    amenities = listOfNotNull(
                        hotel.feature1, hotel.feature2, hotel.feature3, hotel.feature4,
                        hotel.feature5, hotel.feature6, hotel.feature7, hotel.feature8
                    ).filter { it.isNotBlank() },
                    latitude = 0.0, // Default value, as not available
                    longitude = 0.0, // Default value, as not available
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