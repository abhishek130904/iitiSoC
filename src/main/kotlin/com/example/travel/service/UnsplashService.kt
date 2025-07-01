package com.example.travel.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UnsplashService(
    @Value("\${unsplash.api.key}") private val apiKey: String,
    private val restTemplate: RestTemplate = RestTemplate()
) {
    fun getCityImage(city: String): String {
        val url = "https://api.unsplash.com/search/photos?page=1&query=${city}&client_id=$apiKey"
        val response = restTemplate.getForObject(url, Map::class.java)
        val results = response?.get("results") as? List<Map<*, *>>
        return results?.firstOrNull()?.get("urls")?.let { (it as Map<*, *>)["regular"] as String } ?: ""
    }
}
