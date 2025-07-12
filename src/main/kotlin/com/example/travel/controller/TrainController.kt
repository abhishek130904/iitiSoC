package com.example.travel.controller

import com.example.travel.model.TrainEntity
import com.example.travel.service.TrainService
import com.example.travel.service.TrainSearchResultDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trains")
class TrainController(
    private val trainService: TrainService
) {
    @GetMapping("/route/{from}/{to}")
    fun getTrainsPassingThrough(
        @PathVariable from: String,
        @PathVariable to: String
    ): ResponseEntity<List<TrainEntity>> =
        ResponseEntity.ok(trainService.getTrainsPassingThrough(from, to))

    @GetMapping("/number/{trainNo}")
    fun getTrainByNumber(@PathVariable trainNo: String): ResponseEntity<List<TrainEntity>> =
        ResponseEntity.ok(trainService.getTrainByNumber(trainNo))

    @GetMapping("/station/{stationCode}")
    fun getTrainsByStation(@PathVariable stationCode: String): ResponseEntity<List<TrainEntity>> =
        ResponseEntity.ok(trainService.getTrainsByStation(stationCode))

    @GetMapping("/fare/{trainNo}/{from}/{to}/{trainClass}")
    fun getFare(
        @PathVariable trainNo: String,
        @PathVariable from: String,
        @PathVariable to: String,
        @PathVariable trainClass: String
    ): ResponseEntity<Double> {
        val fare = trainService.calculateFare(
            trainService.getDistanceBetweenStations(trainNo, from, to), trainClass)
        return if (fare != null) ResponseEntity.ok(fare) else ResponseEntity.notFound().build()
    }

    @GetMapping("/search/{from}/{to}")
    fun searchTrainsWithFare(
        @PathVariable from: String,
        @PathVariable to: String
    ): ResponseEntity<List<TrainSearchResultDTO>> =
        ResponseEntity.ok(trainService.searchTrainsWithFare(from, to))

    @GetMapping("/debug/from/{from}")
    fun debugFrom(@PathVariable from: String): ResponseEntity<List<TrainEntity>> =
        ResponseEntity.ok(trainService.debugFindByStationCode(from))

    @GetMapping("/debug/train/{trainNo}")
    fun debugTrain(@PathVariable trainNo: String): ResponseEntity<List<TrainEntity>> =
        ResponseEntity.ok(trainService.debugFindByTrainNo(trainNo))
} 