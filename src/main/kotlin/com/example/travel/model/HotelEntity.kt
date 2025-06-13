package com.example.travel.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.Table

@Entity
@Table(name = "hotels")
data class HotelEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val city: String,

    @Column(nullable = false)
    val latitude: Double,

    @Column(nullable = false)
    val longitude: Double,

    @Column(nullable = false)
    val rating: Float,

    @Column(name = "price_per_night", nullable = false)
    val pricePerNight: Double,

    @Column(nullable = false)
    val currency: String,

    val amenities: String?
)