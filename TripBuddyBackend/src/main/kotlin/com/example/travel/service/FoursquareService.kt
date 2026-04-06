package com.example.travel.service

import com.example.travel.network.FoursquareApiClient
import org.springframework.stereotype.Service

@Service
class FoursquareService(private val foursquareApiClient: FoursquareApiClient) {
    fun searchPlaces(query: String?, categories: String?, near: String?, ll: String?, radius: Int?, sort: String?): String? {
        return foursquareApiClient.searchPlaces(query, categories, near, ll, radius, sort)
    }

    fun getCategories(): String? = foursquareApiClient.getCategories()

    fun getTrendingPlaces(ll: String, radius: Int?): String? = foursquareApiClient.getTrendingPlaces(ll, radius)

    fun getPlaceDetails(fsqId: String): String? = foursquareApiClient.getPlaceDetails(fsqId)
} 