package com.example.travel.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.Table

@Entity
@Table(name = "goibibo_hotels_with_random_prices")
data class HotelEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "Hotel_Name", nullable = false)
    val hotelName: String,

    @Column(name = "Hotel_Rating", nullable = false)
    val hotelRating: Double,

    @Column(name = "City", nullable = false)
    val city: String,

    @Column(name = "Feature_1")
    val feature1: String?,

    @Column(name = "Feature_2")
    val feature2: String?,

    @Column(name = "Feature_3")
    val feature3: String?,

    @Column(name = "Feature_4")
    val feature4: String?,

    @Column(name = "Feature_5")
    val feature5: String?,

    @Column(name = "Feature_6")
    val feature6: String?,

    @Column(name = "Feature_7")
    val feature7: String?,

    @Column(name = "Feature_8")
    val feature8: String?,

    @Column(name = "Hotel_Price", nullable = false)
    val hotelPrice: Double
)