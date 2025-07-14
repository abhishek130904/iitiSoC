package com.example.travel.repository

import com.example.travel.model.TrainEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrainRepository : JpaRepository<TrainEntity, String> {
    fun findByTrainNo(trainNo: String): List<TrainEntity>
    fun findByStationCodeIn(stationCodes: List<String>): List<TrainEntity>
}