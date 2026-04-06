package com.example.travel.controller

import com.example.travel.service.TrainService
import com.example.travel.service.StationDTO
import com.example.travel.service.TrainSearchResultDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trains")
class TrainController(private val trainService: TrainService) {

    @GetMapping("/search")
    fun searchTrains(
        @RequestParam from: String,
        @RequestParam to: String
    ): List<TrainSearchResultDTO> {
        return trainService.findTrainsBetweenStations(from, to)
    }

    @GetMapping("/stations")
    fun getStations(@RequestParam(required = false) query: String?): List<StationDTO> {
        return if (query.isNullOrBlank()) {
            trainService.getDistinctStations()
        } else {
            trainService.searchStationsByName(query)
        }
    }
} 