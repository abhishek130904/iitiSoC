package com.example.travel.controller

import com.example.travel.model.TrainEntity
import com.example.travel.service.TrainService
import com.example.travel.service.TrainWithDistanceDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trains")
class TrainController(private val trainService: TrainService) {

    @GetMapping
    fun getAllTrains(): List<TrainEntity> = trainService.getAllTrains()

    @GetMapping("/search")
    fun getTrainsBetweenStations(
        @RequestParam from: String,
        @RequestParam to: String
    ): List<TrainWithDistanceDTO> = trainService.getTrainsWithDistanceBetweenStations(from, to)

    @GetMapping("/{trainNo}")
    fun getTrainByNumber(@PathVariable trainNo: String): List<TrainEntity> =
        trainService.getTrainsByNumber(trainNo)

    @GetMapping("/distance")
    fun getDistanceBetweenStations(
        @RequestParam trainNo: String,
        @RequestParam from: String,
        @RequestParam to: String
    ): Int? = trainService.getDistanceBetweenStations(trainNo, from, to)
} 