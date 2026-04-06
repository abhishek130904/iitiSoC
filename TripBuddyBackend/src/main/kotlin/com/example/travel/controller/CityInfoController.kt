package com.example.travel.controller

import com.example.travel.service.UnsplashService
import com.example.travel.service.WikipediaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/city")
class CityInfoController(
    private val wikipediaService: WikipediaService,
    private val unsplashService: UnsplashService
) {
    @GetMapping("/{cityName}")
    fun getCityInfo(@PathVariable cityName: String): Map<String, String> {
        val description = wikipediaService.getCityDescription(cityName)
        val imageUrl = unsplashService.getCityImage(cityName)
        return mapOf(
            "city" to cityName,
            "description" to description,
            "imageUrl" to imageUrl
        )
    }
}
