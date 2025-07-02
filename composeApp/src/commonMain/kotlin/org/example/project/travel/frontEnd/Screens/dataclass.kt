package org.example.project.travel.frontEnd.Screens


data class TravelInfo(
    val name: String,
    val phone: String,
    val adults: String,
    val children: String,
    val tripDate: String,
    val destination: String,
    val duration: String,
    val totalCost: String,
    val consultant: String,
    val consultantPhone: String,
    val partner: String,
    val website: String,
    val agencyPhone: String,
    val agencyEmail: String,
    val hotels: List<HotelInfo>,
    val itinerary: List<ItineraryDay>,
    val inclusions: List<String>,
    val exclusions: List<String>
)

data class HotelInfo(
    val city: String,
    val name: String,
    val nights: String,
    val mealPlan: String
)

data class ItineraryDay(
    val dayTitle: String,
    val description: String
)
