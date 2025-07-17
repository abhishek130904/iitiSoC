package com.example.travel.service

import com.example.travel.model.TrainEntity
import com.example.travel.repository.TrainRepository
import org.springframework.stereotype.Service

@Service
class TrainService(private val trainRepository: TrainRepository) {
    fun findTrainsBetweenStations(fromStation: String, toStation: String): List<TrainSearchResultDTO> {
        return trainRepository.findTrainPairsBetweenStations(fromStation, toStation).map { pair ->
            val from = pair[0]
            val to = pair[1]
            TrainSearchResultDTO(
                train_no = from.train_no,
                train_name = from.train_name,
                from_station_code = from.station_code,
                from_station_name = from.station_name,
                to_station_code = to.station_code,
                to_station_name = to.station_name,
                from_departure_time = from.departure_time,
                to_arrival_time = to.arrival_time,
                distance = to.distance - from.distance
            )
        }
    }
}

// DTO for train search result with distance
data class TrainSearchResultDTO(
    val train_no: String,
    val train_name: String,
    val from_station_code: String,
    val from_station_name: String,
    val to_station_code: String,
    val to_station_name: String,
    val from_departure_time: String,
    val to_arrival_time: String,
    val distance: Int
) 