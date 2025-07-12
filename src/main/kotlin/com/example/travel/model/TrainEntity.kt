package com.example.travel.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalTime

@Entity
@Table(name = "train")
data class TrainEntity(
    @Id
    @Column(name = "train_no")
    val trainNo: String,

    @Column(name = "train_name")
    val trainName: String,

    @Column(name = "stop_number")
    val stopNumber: Int,

    @Column(name = "station_code")
    val stationCode: String,

    @Column(name = "station_name")
    val stationName: String,

    @Column(name = "arrival_time")
    val arrivalTime: LocalTime?,

    @Column(name = "departure_time")
    val departureTime: LocalTime?,

    @Column(name = "distance")
    val distance: Int,

    @Column(name = "source_station_code")
    val sourceStationCode: String,

    @Column(name = "source_station_name")
    val sourceStationName: String,

    @Column(name = "destination_station_code")
    val destinationStationCode: String,

    @Column(name = "destination_station_name")
    val destinationStationName: String
) 