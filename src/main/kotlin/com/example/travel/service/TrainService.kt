package com.example.travel.service

import com.example.travel.model.TrainEntity
import com.example.travel.repository.TrainRepository
import org.springframework.stereotype.Service

// DTO for train with distance
data class TrainWithDistanceDTO(
    val trainNo: String,
    val trainName: String,
    val fromStation: String,
    val toStation: String,
    val distance: Int,
    val stops: List<TrainEntity>
)

@Service
class TrainService(private val trainRepository: TrainRepository) {
    fun getAllTrains(): List<TrainEntity> = trainRepository.findAll()

    fun getTrainsByNumber(trainNo: String): List<TrainEntity> = trainRepository.findByTrainNo(trainNo)

    fun getDistanceBetweenStations(trainNo: String, from: String, to: String): Int? {
        val stops = trainRepository.findByTrainNo(trainNo)
        val fromStop = stops.find { it.stationCode == from }
        val toStop = stops.find { it.stationCode == to }
        return if (fromStop != null && toStop != null) {
            kotlin.math.abs(toStop.distance - fromStop.distance)
        } else null
    }

    // Use only findAll() and do all filtering/grouping in memory
    fun getTrainsWithDistanceBetweenStations(from: String, to: String): List<TrainWithDistanceDTO> {
        val allStops = trainRepository.findAll()
        return allStops
            .groupBy { it.trainNo }
            .mapNotNull { (trainNo, stopsList) ->
                val sortedStops = stopsList.sortedBy { it.stopNumber }
                val fromStop = sortedStops.find { it.stationCode == from }
                val toStop = sortedStops.find { it.stationCode == to }
                if (fromStop != null && toStop != null && fromStop.stopNumber < toStop.stopNumber) {
                    TrainWithDistanceDTO(
                        trainNo = trainNo,
                        trainName = fromStop.trainName,
                        fromStation = from,
                        toStation = to,
                        distance = kotlin.math.abs(toStop.distance - fromStop.distance),
                        stops = sortedStops
                    )
                } else null
            }
    }
}