package com.example.travel.repository

import com.example.travel.model.TrainEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TrainRepository : JpaRepository<TrainEntity, String> {
    fun findBySourceStationCodeAndDestinationStationCode(source: String, destination: String): List<TrainEntity>
    fun findByTrainNo(trainNo: String): List<TrainEntity>
    fun findByStationCode(stationCode: String): List<TrainEntity>

    @Query("""
        SELECT t1 FROM TrainEntity t1, TrainEntity t2
        WHERE t1.trainNo = t2.trainNo
          AND t1.stationCode = :from
          AND t2.stationCode = :to
          AND t1.stopNumber < t2.stopNumber
    """)
    fun findTrainsPassingThrough(
        @Param("from") from: String,
        @Param("to") to: String
    ): List<TrainEntity>

    @Query("""
        SELECT DISTINCT t1.trainNo FROM TrainEntity t1, TrainEntity t2
        WHERE t1.trainNo = t2.trainNo
          AND LOWER(t1.stationCode) = LOWER(:from)
          AND LOWER(t2.stationCode) = LOWER(:to)
          AND t1.stopNumber < t2.stopNumber
    """)
    fun findTrainNumbersPassingThrough(
        @Param("from") from: String,
        @Param("to") to: String
    ): List<String>
} 