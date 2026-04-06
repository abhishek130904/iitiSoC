package com.example.travel.repository


import com.example.travel.model.HotelEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HotelRepository : JpaRepository<HotelEntity, Long> {
    fun findByCityIgnoreCase(city: String): List<HotelEntity>
}