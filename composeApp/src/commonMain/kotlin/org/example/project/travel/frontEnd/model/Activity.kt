package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable

@Serializable
data class TripActivity(
    val time: String,
    val name: String,
    val description: String,
    val cost: Int
) 