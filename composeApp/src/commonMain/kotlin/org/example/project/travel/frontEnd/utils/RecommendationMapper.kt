package org.example.project.travel.frontEnd.utils

import org.example.project.travel.frontEnd.model.RecommendationConfig


object RecommendationMapper {
    fun mapDataToGoals() {
        RecommendationConfig.goals.forEach { goal ->
            println("Mapping for ${goal.category}:")
            println("  Data Sources: ${goal.dataSources.joinToString()}")
            println("  API Sources: ${goal.apiSources.joinToString()}")
            println("  Priority: ${goal.priority}")
            when (goal.category) {
                "Similar Destinations" -> println("  Strategy: Match cities by travelStyle and location type")
                "Better Hotels or Restaurants" -> println("  Strategy: Filter by ratings > current rating")
                "Missed Activities" -> println("  Strategy: Compare against city activity list")
                "Personalized Packing Tips" -> println("  Strategy: Use weather and activity data")
                "Deals or Offers for Future Trips" -> println("  Strategy: Target budgetRange with promotions")
            }
            println("---")
        }
    }
}