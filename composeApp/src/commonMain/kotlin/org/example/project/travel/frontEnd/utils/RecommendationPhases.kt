package org.example.project.travel.frontEnd.utils

import org.example.project.travel.frontEnd.model.RecommendationConfig


object RecommendationPhases {
    fun definePhases() {
        println("Recommendation Rollout Plan:")
        RecommendationConfig.goals.groupBy { it.priority }.forEach { (priority, goals) ->
            println("Phase $priority:")
            goals.forEach { goal ->
                println("  - ${goal.category}")
            }
            println()
        }
    }
}