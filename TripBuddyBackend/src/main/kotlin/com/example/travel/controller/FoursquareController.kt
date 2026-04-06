package com.example.travel.controller

import com.example.travel.service.FoursquareService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/foursquare")
class FoursquareController(private val foursquareService: FoursquareService) {
    @GetMapping("/search")
    fun searchPlaces(
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) categories: String?,
        @RequestParam(required = false) near: String?,
        @RequestParam(required = false) ll: String?,
        @RequestParam(required = false) radius: Int?,
        @RequestParam(required = false) sort: String?
    ): String? = foursquareService.searchPlaces(query, categories, near, ll, radius, sort)

    @GetMapping("/categories")
    fun getCategories(): String? = foursquareService.getCategories()

    @GetMapping("/trending")
    fun getTrendingPlaces(
        @RequestParam ll: String,
        @RequestParam(required = false) radius: Int?
    ): String? = foursquareService.getTrendingPlaces(ll, radius)

    @GetMapping("/details/{fsqId}")
    fun getPlaceDetails(@PathVariable fsqId: String): String? = foursquareService.getPlaceDetails(fsqId)
}
