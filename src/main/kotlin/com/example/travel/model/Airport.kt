package com.example.travel.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "airports")
data class Airport(
    @Id
    val iataCode: String,
    val city: String,
    val country: String,
    val name: String
)
