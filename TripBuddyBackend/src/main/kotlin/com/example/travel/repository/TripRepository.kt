package com.example.travel.repository

import com.example.travel.model.TripEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TripRepository : JpaRepository<TripEntity, Long> {
    fun findByUserId(userId: String): List<TripEntity>

    @Query("SELECT t FROM TripEntity t WHERE t.userId = :userId ORDER BY t.createdAt DESC")
    fun findByUserIdOrderByCreatedAtDesc(@Param("userId") userId: String): List<TripEntity>
}