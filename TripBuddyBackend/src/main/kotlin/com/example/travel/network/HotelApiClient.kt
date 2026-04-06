package com.example.travel.network


import com.example.travel.model.HotelEntity
import com.example.travel.repository.HotelRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class HotelApiClient(
    private val restTemplate: RestTemplate,
    private val hotelRepository: HotelRepository
) {

    fun searchHotelsByCity(city: String): List<HotelEntity> {
        return hotelRepository.findByCityIgnoreCase(city)
    }
}