package org.example.project.travel.frontEnd.model

import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val type: String,
    val venue: String?,
    val time: String,
    val cost: Double
) 