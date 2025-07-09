package org.example.project.travel.frontEnd.utils

object RecommendationPersonalization {
    fun defineRules() {
        println("Personalization Rules:")
        println("  - Filter destinations by travelStyle (e.g., adventure -> hiking cities)")
        println("  - Match budgetRange to deal offers (e.g., $0-$500 -> affordable options)")
        println("  - Adjust for season based on tripEndDate (e.g., winter -> warm destinations)")
    }
}