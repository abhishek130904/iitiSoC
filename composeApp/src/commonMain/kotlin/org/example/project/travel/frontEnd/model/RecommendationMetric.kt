package org.example.project.travel.frontEnd.model

data class RecommendationMetric(
    val name: String,
    val targetValue: Double,
    val description: String
)

object MetricConfig {
    val metrics = listOf(
        RecommendationMetric(
            name = "Engagement Rate",
            targetValue = 20.0, // 20% of users interacting
            description = "Percentage of users clicking recommendations"
        ),
        RecommendationMetric(
            name = "Conversion Rate",
            targetValue = 5.0, // 5% of users booking
            description = "Percentage of users booking recommended trips"
        ),
        RecommendationMetric(
            name = "Satisfaction Score",
            targetValue = 4.0, // Average rating out of 5
            description = "Average user rating of recommendations"
        )
    )
}