package com.example.travel.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "trips")
data class TripEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: String, // Firebase UID or custom user ID

    @Column(nullable = false)
    val cityName: String, // Store the city name directly

    @Column(nullable = false)
    val flightId: String, // Reference to a FlightEntity if normalized

    @Column(nullable = false)
    val hotelName: String, // Store the hotel name directly

    @ElementCollection
    @CollectionTable(name = "trip_activities", joinColumns = [JoinColumn(name = "trip_id")])
    val activities: List<ActivityEntity> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "trip_meals", joinColumns = [JoinColumn(name = "trip_id")])
    val meals: List<MealEntity> = emptyList(),

    @Column(columnDefinition = "TEXT")
    val notes: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)

@Embeddable
data class ActivityEntity(
    val time: String,
    val name: String,
    val description: String,
    val cost: Double
)

@Embeddable
data class MealEntity(
    val type: String,
    val venue: String?,
    val time: String,
    val cost: Double
)