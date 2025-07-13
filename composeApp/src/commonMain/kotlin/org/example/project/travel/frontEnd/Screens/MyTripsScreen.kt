package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.example.project.travel.frontend.network.TravelApi
import org.example.project.travel.frontEnd.pdf.TripSummary
import org.example.project.travel.frontEnd.pdf.generateTripSummaryPdf
import org.example.project.travel.frontEnd.pdf.saveTripSummaryPdfFile

@Serializable
data class TripActivity(
    val time: String,
    val name: String,
    val description: String,
    val cost: Double
)

@Serializable
data class TripMeal(
    val type: String,
    val venue: String,
    val time: String,
    val cost: Double
)

@Serializable
data class TripItinerary(
    val id: Long,
    val userId: String,
    val cityName: String,
    val flightId: String,
    val hotelName: String,
    val activities: List<TripActivity>,
    val meals: List<TripMeal>,
    val notes: String? = null,
    val createdAt: String
)

@Composable
fun MyTripsScreen(userId: String) {
    var trips by remember { mutableStateOf<List<TripItinerary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(userId) {
        try {
            trips = TravelApi.getMyTrips(userId)
            isLoading = false
        } catch (e: Exception) {
            error = e.message
            isLoading = false
        }
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
        error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
        trips.isEmpty() -> Text("No trips found.")
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips) { trip ->
                TripCard(trip, context)
            }
        }
    }
}

@Composable
fun TripCard(trip: TripItinerary, context: android.content.Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("City: ${trip.cityName}", fontWeight = FontWeight.Bold)
            Text("Flight: ${trip.flightId}")
            Text("Hotel: ${trip.hotelName}")
            Text("Created: ${trip.createdAt}")
            Spacer(Modifier.height(8.dp))
            Text("Activities:", fontWeight = FontWeight.SemiBold)
            trip.activities.forEach {
                Text("- ${it.time}: ${it.name} (${it.description})")
            }
            Spacer(Modifier.height(4.dp))
            Text("Meals:", fontWeight = FontWeight.SemiBold)
            trip.meals.forEach {
                Text("- ${it.type} at ${it.venue} (${it.time}), ₹${"%.2f".format(it.cost)}")
            }
            trip.notes?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text("Notes: $it")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val summary = TripSummary(
                        destination = trip.cityName,
                        dates = trip.createdAt,
                        flightDetails = trip.flightId,
                        hotelDetails = trip.hotelName,
                        activities = trip.activities.joinToString { it.name },
                        meals = trip.meals.joinToString { it.type },
                        costBreakdown = "", // Add cost breakdown if available
                        notes = trip.notes
                    )
                    val pdfBytes = generateTripSummaryPdf(summary)
                    saveTripSummaryPdfFile(context, pdfBytes, "TripSummary_${trip.id}.pdf")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download Summary PDF")
            }
        }
    }
} 