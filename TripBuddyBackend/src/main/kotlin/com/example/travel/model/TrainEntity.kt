package com.example.travel.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "train")
data class TrainEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val train_no: String,
    val train_name: String,
    val stop_number: Int,
    val station_code: String,
    val station_name: String,
    val arrival_time: String,
    val departure_time: String,
    val distance: Int,
    val source_station_code: String,
    val source_station_name: String,
    val destination_station_code: String,
    val destination_station_name: String
) 