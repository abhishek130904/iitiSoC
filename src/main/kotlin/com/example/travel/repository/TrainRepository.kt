package com.example.travel.repository

import com.example.travel.model.TrainEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TrainRepository : CrudRepository<TrainEntity, Long> {
    @Query("""
        SELECT t1, t2 FROM TrainEntity t1, TrainEntity t2
        WHERE t1.train_no = t2.train_no
          AND t1.station_code = :fromStation
          AND t2.station_code = :toStation
          AND t1.stop_number < t2.stop_number
    """)
    fun findTrainPairsBetweenStations(fromStation: String, toStation: String): List<Array<TrainEntity>>

    @Query("SELECT DISTINCT t.station_code, t.station_name FROM TrainEntity t")
    fun findDistinctStations(): List<Array<Any>>

    @Query("SELECT DISTINCT t.station_code, t.station_name FROM TrainEntity t WHERE LOWER(t.station_name) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun searchStationsByName(query: String): List<Array<Any>>
} 