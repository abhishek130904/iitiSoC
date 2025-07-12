package com.example.travel.service

import com.example.travel.model.TrainEntity
import com.example.travel.repository.TrainRepository
import org.springframework.stereotype.Service

// DTO for search results
data class TrainSearchResultDTO(
    val trainNo: String,
    val trainName: String,
    val departureFrom: String?,
    val arrivalTo: String?,
    val distance: Int?,
    val fareSL: Double?,
    val fare3A: Double?,
    val fare2A: Double?,
    val fare1A: Double?
)

@Service
class TrainService(private val trainRepository: TrainRepository) {
    fun getTrainsByRoute(source: String, destination: String): List<TrainEntity> =
        trainRepository.findBySourceStationCodeAndDestinationStationCode(source, destination)

    fun getTrainByNumber(trainNo: String): List<TrainEntity> =
        trainRepository.findByTrainNo(trainNo)

    fun getTrainsByStation(stationCode: String): List<TrainEntity> =
        trainRepository.findByStationCode(stationCode)

    fun getTrainsPassingThrough(from: String, to: String): List<TrainEntity> =
        trainRepository.findTrainsPassingThrough(from, to)

    fun getDistanceBetweenStations(trainNo: String, from: String, to: String): Int? {
        val stops = trainRepository.findByTrainNo(trainNo)
        val fromStop = stops.find { it.stationCode.equals(from, ignoreCase = true) }
        val toStop = stops.find { it.stationCode.equals(to, ignoreCase = true) }
        return if (fromStop != null && toStop != null) {
            toStop.distance - fromStop.distance
        } else {
            null
        }
    }

    private val farePerKm = mapOf(
        "SL" to 0.7,   // Sleeper
        "3A" to 1.75,  // AC 3 Tier
        "2A" to 2.75,  // AC 2 Tier
        "1A" to 4.5    // AC 1 Tier
    )

    fun calculateFare(distance: Int?, trainClass: String): Double? {
        val rate = farePerKm[trainClass] ?: 1.0
        return distance?.let { it * rate }
    }

    fun searchTrainsWithFare(from: String, to: String): List<TrainSearchResultDTO> {
        val fromStops = trainRepository.findByStationCode(from)
        val results = mutableListOf<TrainSearchResultDTO>()
        val processedTrains = mutableSetOf<String>()

        for (fromStop in fromStops) {
            val trainNo = fromStop.trainNo
            if (processedTrains.contains(trainNo)) continue // Avoid duplicates
            val stops = trainRepository.findByTrainNo(trainNo)
            val toStops = stops.filter { it.stationCode.equals(to, ignoreCase = true) }
            for (toStop in toStops) {
                if (fromStop.stopNumber < toStop.stopNumber) {
                    val distance = kotlin.math.abs(toStop.distance - fromStop.distance)
                    results.add(
                        TrainSearchResultDTO(
                            trainNo = trainNo,
                            trainName = fromStop.trainName,
                            departureFrom = fromStop.departureTime?.toString(),
                            arrivalTo = toStop.arrivalTime?.toString(),
                            distance = distance,
                            fareSL = calculateFare(distance, "SL"),
                            fare3A = calculateFare(distance, "3A"),
                            fare2A = calculateFare(distance, "2A"),
                            fare1A = calculateFare(distance, "1A")
                        )
                    )
                    processedTrains.add(trainNo)
                    break // Only add the first valid pair for each train
                }
            }
        }
        return results
    }

    fun debugFindByStationCode(stationCode: String): List<TrainEntity> {
        return trainRepository.findByStationCode(stationCode)
    }

    fun debugFindByTrainNo(trainNo: String): List<TrainEntity> {
        return trainRepository.findByTrainNo(trainNo)
    }
} 