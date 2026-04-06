package com.example.travel.controller

import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.service.HotelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HotelController(private val hotelService: HotelService) {

    @GetMapping("/api/hotels")
    fun getHotels(
        @RequestParam("city") city: String,
        @RequestParam(value = "sortBy", required = false, defaultValue = "rating") sortBy: String,
        @RequestParam(value = "order", required = false, defaultValue = "desc") order: String,
        @RequestParam(value = "minRating", required = false) minRating: Float?
    ): ResponseEntity<List<AccommodationDTO>> {
        val hotels = hotelService.searchHotelsByCity(city, sortBy, order, minRating)
        return if (hotels.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(hotels)
        }
    }
}