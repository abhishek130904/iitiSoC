package org.example.project.travel.frontEnd.model

data class RecommendationGoal(
    val category: String,
    val dataSources: List<String>,
    val apiSources: List<String> = emptyList(),
    val priority: Int
)

object RecommendationConfig {
    val goals = listOf(
        RecommendationGoal(
            category = "Similar Destinations",
            dataSources = listOf("citiesVisited", "travelStyle"),
            apiSources = listOf("/api/airports/cities"),
            priority = 1
        ),
        RecommendationGoal(
            category = "Better Hotels or Restaurants",
            dataSources = listOf("hotelsStayed", "ratings"),
            apiSources = listOf("/api/hotel-ratings"), // Placeholder for external hotel API
            priority = 2
        ),
        RecommendationGoal(
            category = "Missed Activities",
            dataSources = listOf("activitiesDone", "citiesVisited"),
            apiSources = listOf("/api/recommendations"),
            priority = 3
        ),
        RecommendationGoal(
            category = "Personalized Packing Tips",
            dataSources = listOf("activitiesDone", "travelStyle", "budgetRange"),
            apiSources = listOf("https://api.openweathermap.org"), // Weather API example
            priority = 4
        ),
        RecommendationGoal(
            category = "Deals or Offers for Future Trips",
            dataSources = listOf("citiesVisited", "budgetRange"),
            apiSources = listOf("/api/flights/search", "/api/promotions"), // Flight and deal APIs
            priority = 1
        )
    )
}