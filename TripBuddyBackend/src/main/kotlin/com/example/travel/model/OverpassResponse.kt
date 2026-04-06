package com.example.travel.model

import kotlinx.serialization.Serializable

@Serializable
data class OverpassResponse(
    val elements: List<Element>
)

@Serializable
data class Element(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>
)