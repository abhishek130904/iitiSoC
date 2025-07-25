package com.example.travel.network

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class FoursquareApiClient(
    @Value(" ouredsquare.api.key") private val apiKey: String
) {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.foursquare.com/v3/places")
        .defaultHeader("Authorization", apiKey)
        .build()

    fun searchPlaces(query: String?, categories: String?, near: String?, ll: String?, radius: Int?, sort: String?): String? {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search")
                    .apply {
                        if (!query.isNullOrBlank()) queryParam("query", query)
                        if (!categories.isNullOrBlank()) queryParam("categories", categories)
                        if (!near.isNullOrBlank()) queryParam("near", near)
                        if (!ll.isNullOrBlank()) queryParam("ll", ll)
                        if (radius != null) queryParam("radius", radius)
                        if (!sort.isNullOrBlank()) queryParam("sort", sort)
                    }
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

    fun getPlaceDetails(fsqId: String): String? =
        webClient.get()
            .uri("/{fsq_id}", fsqId)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

    fun getCategories(): String? =
        webClient.get()
            .uri("/categories")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

    fun getTrendingPlaces(ll: String, radius: Int?): String? =
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/trending")
                    .queryParam("ll", ll)
                    .apply { if (radius != null) queryParam("radius", radius) }
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
} 