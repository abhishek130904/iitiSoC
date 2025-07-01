package com.example.travel.repository

import com.example.travel.model.TripEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TripRepository : JpaRepository<TripEntity, Long> {
    fun findByUserId(userId: String): List<TripEntity>
}