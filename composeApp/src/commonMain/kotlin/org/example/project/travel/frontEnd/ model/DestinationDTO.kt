package org.example.project.travel.frontend.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDetailsResponse(
    val id: Long,
    val country: String,
    val city: String,
    val state: String,
    val cityCode: Long,
    val activities: List<Activity>
)

@Serializable
data class Activity(
    val properties: Properties
)

@Serializable
data class Properties(
    val name: String,
    val kinds: String,
    val lat: Double? = null,
    val lon: Double? = null
)

@Serializable
data class DestinationCity(
    val id: Long,
    val city: String,
    val state: String,
    val country: String,
    val cityCode: Long
)

// Wikipedia and Unsplash models (simplified)
@Serializable
data class WikipediaResponse(
    val query: WikipediaQuery
)

@Serializable
data class WikipediaQuery(
    val pages: Map<String, WikipediaPage>
)

@Serializable
data class WikipediaPage(
    val title: String,
    val extract: String
)

@Serializable
data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)

@Serializable
data class UnsplashPhoto(
    val urls: UnsplashUrls,
    val alt_description: String?
)

@Serializable
data class UnsplashUrls(
    val regular: String
)