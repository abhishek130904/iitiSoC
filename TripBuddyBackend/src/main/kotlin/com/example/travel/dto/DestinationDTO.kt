package com.example.travel.dto

import jakarta.persistence.*
import kotlinx.serialization.Serializable

@Entity
@Table(name = "destination_cities")
data class DestinationCity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val country: String,
    val city: String,
    val state: String,
    @Column(name = "city_code")
    val cityCode: Long
)
