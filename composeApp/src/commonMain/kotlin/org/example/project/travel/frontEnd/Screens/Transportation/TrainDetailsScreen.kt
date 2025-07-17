package org.example.project.travel.frontend.Screens.Transportation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel.network.fetchTrains
import com.example.travel.network.TrainSearchResultDTO
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

fun calculateFare(distance: Int, coach: String): Int {
    val (base, perKm) = when (coach) {
        "SL" -> 100 to 0.45
        "3E" -> 150 to 1.10
        "3A" -> 200 to 1.20
        "2A" -> 300 to 1.70
        "1A" -> 500 to 2.90
        else -> 0 to 0.0
    }
    return (base + distance * perKm).toInt()
}

@Composable
fun FareCalculator(distance: Int) {
    var selectedCoach by remember { mutableStateOf("SL") }
    val coachTypes = listOf("SL", "3E", "3A", "2A", "1A")
    val fare = calculateFare(distance, selectedCoach)

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("Fare Calculator", fontWeight = FontWeight.Bold, color = Color(0xFF176FF3))
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            coachTypes.forEach { coach ->
                Button(
                    onClick = { selectedCoach = coach },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCoach == coach) Color(0xFF176FF3) else Color.LightGray
                    ),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(coach, color = Color.White)
                }
            }
        }
        Text("Distance: $distance km", color = Color.Gray)
        Text("Fare for $selectedCoach: ₹$fare", fontWeight = FontWeight.Bold, color = Color(0xFF176FF3))
    }
}

@Composable
fun TrainDetailsScreen(
    fromStation: String,
    toStation: String
) {
    var trains by remember { mutableStateOf<List<TrainSearchResultDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(fromStation, toStation) {
        isLoading = true
        error = null
        coroutineScope.launch {
            try {
                trains = fetchTrains(fromStation, toStation)
            } catch (e: Exception) {
                error = "Failed to load trains."
                trains = emptyList()
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Available Trains",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF176FF3),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color(0xFF176FF3))
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(error!!, color = Color.Red)
                }
            }
            trains.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No trains found.", color = Color.Gray)
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(trains) { train ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "${train.train_no} - ${train.train_name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF176FF3)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("From: ${train.from_station_name} (${train.from_station_code})    To: ${train.to_station_name} (${train.to_station_code})", color = Color.Black, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Departure: ${train.from_departure_time}    Arrival: ${train.to_arrival_time}", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Distance: ${train.distance} km", color = Color.Gray, fontSize = 13.sp)
                                FareCalculator(train.distance)
                            }
                        }
                    }
                }
            }
        }
    }
} 