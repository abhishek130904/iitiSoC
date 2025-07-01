package com.example.travel.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class WikipediaService(private val restTemplate: RestTemplate = RestTemplate()) {

    fun getCityDescription(city: String): String {
        val url = "https://en.wikipedia.org/api/rest_v1/page/summary/${city.replace(" ", "_")}"
        val response = restTemplate.getForObject(url, Map::class.java)
        return response?.get("extract") as? String ?: "Description not found."
    }
}
