package org.example.project.travel.frontend.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data classes remain unchanged
data class ItineraryDay(
    val date: String,
    val activities: List<Activity>,
    val transport: Transport?,
    val accommodation: Accommodation,
    val meals: List<Meal>
)

data class Activity(
    val time: String,
    val name: String,
    val description: String,
    val cost: Double
)

data class Transport(
    val type: String,
    val details: String,
    val time: String,
    val cost: Double
)

data class Accommodation(
    val name: String,
    val type: String,
    val checkIn: String,
    val checkOut: String,
    val cost: Double
)

data class Meal(
    val type: String,
    val venue: String?,
    val time: String,
    val cost: Double
)

private val primaryBlue = Color(23, 111, 243)
private val white = Color.White

@Composable
fun TripItineraryScreen(
    onNavigateToTransport: () -> Unit = {},
    onNavigateToAccommodation: () -> Unit = {},
    onNavigateToActivities: () -> Unit = {},
    onNavigateToDining: () -> Unit = {},
    onProceedToBooking: () -> Unit = {}
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var tripNotes by remember { mutableStateOf("") }

    // Sample data - Replace with ViewModel in production
    val itinerary = remember {
        listOf(
            ItineraryDay(
                date = "Day 1 - Dec 15, 2024",
                activities = listOf(
                    Activity(
                        time = "14:00",
                        name = "Hotel Check-in",
                        description = "Taj Resort & Spa",
                        cost = 0.0
                    ),
                    Activity(
                        time = "16:00",
                        name = "Beach Walk",
                        description = "Evening stroll at Calangute Beach",
                        cost = 0.0
                    )
                ),
                transport = Transport(
                    type = "Flight",
                    details = "Air India AI-123",
                    time = "10:30 AM",
                    cost = 350.0
                ),
                accommodation = Accommodation(
                    name = "Taj Resort & Spa",
                    type = "Luxury Hotel",
                    checkIn = "14:00",
                    checkOut = "12:00",
                    cost = 200.0
                ),
                meals = listOf(
                    Meal(
                        type = "Dinner",
                        venue = "Hotel Restaurant",
                        time = "19:30",
                        cost = 30.0
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        // Custom TopBar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = primaryBlue,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Trip Itinerary",
                    color = white,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Quick Actions
            item {
                QuickActionsSection(
                    onNavigateToTransport = onNavigateToTransport,
                    onNavigateToAccommodation = onNavigateToAccommodation,
                    onNavigateToActivities = onNavigateToActivities,
                    onNavigateToDining = onNavigateToDining
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Budget Overview Section
            item {
                BudgetOverviewSection(itinerary)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Day-wise Itinerary
            items(itinerary) { day ->
                DayItineraryCard(day)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Notes Section
            item {
                NotesSection(
                    notes = tripNotes,
                    onNotesChange = { tripNotes = it }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Proceed Button
            item {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Proceed to Booking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = white
                    )
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = white,
            titleContentColor = primaryBlue,
            textContentColor = Color(0xFF333333),
            title = { Text("Proceed to Booking?", fontWeight = FontWeight.SemiBold) },
            text = { Text("You will be redirected to partner websites to complete your bookings. Continue?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onProceedToBooking()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                ) {
                    Text("Proceed", color = white)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = primaryBlue)
                }
            }
        )
    }
}

@Composable
private fun QuickActionsSection(
    onNavigateToTransport: () -> Unit,
    onNavigateToAccommodation: () -> Unit,
    onNavigateToActivities: () -> Unit,
    onNavigateToDining: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionButton(
            icon = Icons.Default.Flight,
            label = "Transport",
            onClick = onNavigateToTransport
        )
        QuickActionButton(
            icon = Icons.Default.Hotel,
            label = "Stays",
            onClick = onNavigateToAccommodation
        )
        QuickActionButton(
            icon = Icons.Default.LocalActivity,
            label = "Activities",
            onClick = onNavigateToActivities
        )
        QuickActionButton(
            icon = Icons.Default.Restaurant,
            label = "Dining",
            onClick = onNavigateToDining
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(primaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = primaryBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            fontSize = 12.sp,
            color = primaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BudgetOverviewSection(itinerary: List<ItineraryDay>) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = white,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Budget Overview",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotationState),
                    tint = primaryBlue
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    val transport = itinerary.sumOf { it.transport?.cost ?: 0.0 }
                    val accommodation = itinerary.sumOf { it.accommodation.cost }
                    val activities = itinerary.sumOf { day -> 
                        day.activities.sumOf { it.cost }
                    }
                    val meals = itinerary.sumOf { day ->
                        day.meals.sumOf { it.cost }
                    }
                    val total = transport + accommodation + activities + meals

                    BudgetItem("Transport", transport)
                    BudgetItem("Accommodation", accommodation)
                    BudgetItem("Activities", activities)
                    BudgetItem("Meals", meals)
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = primaryBlue.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )
                    BudgetItem("Total", total, true)
                }
            }
        }
    }
}

@Composable
private fun BudgetItem(
    title: String,
    amount: Double,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (title) {
                    "Transport" -> Icons.Default.Flight
                    "Accommodation" -> Icons.Default.Hotel
                    "Activities" -> Icons.Default.LocalActivity
                    "Meals" -> Icons.Default.Restaurant
                    else -> Icons.Default.AttachMoney
                },
                contentDescription = title,
                tint = if (isTotal) primaryBlue else Color(0xFF333333),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontWeight = if (isTotal) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isTotal) primaryBlue else Color(0xFF333333)
            )
        }
//        Text(
//            text = "$${"%.2f".format(amount)}",
//            fontWeight = if (isTotal) FontWeight.SemiBold else FontWeight.Normal,
//            color = if (isTotal) primaryBlue else Color(0xFF333333)
//        )
    }
}

@Composable
private fun DayItineraryCard(day: ItineraryDay) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = white,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    day.date,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotationState),
                    tint = primaryBlue
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    day.transport?.let { transport ->
                        ItemSection(
                            title = "Transport",
                            icon = Icons.Default.Flight
                        ) {
                            ItineraryItemContent("${transport.type}: ${transport.details}")
                            ItineraryItemContent("Time: ${transport.time}")
                            ItineraryItemContent("Cost: $${transport.cost}")
                        }
                    }

                    ItemSection(
                        title = "Accommodation",
                        icon = Icons.Default.Hotel
                    ) {
                        ItineraryItemContent(day.accommodation.name)
                        ItineraryItemContent("Check-in: ${day.accommodation.checkIn}")
                        ItineraryItemContent("Check-out: ${day.accommodation.checkOut}")
                        ItineraryItemContent("Cost: $${day.accommodation.cost}/night")
                    }

                    if (day.activities.isNotEmpty()) {
                        ItemSection(
                            title = "Activities",
                            icon = Icons.Default.LocalActivity
                        ) {
                            day.activities.forEach { activity ->
                                ItineraryItemContent("${activity.time} - ${activity.name}")
                                if (activity.cost > 0) {
                                    ItineraryItemContent("Cost: $${activity.cost}")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    if (day.meals.isNotEmpty()) {
                        ItemSection(
                            title = "Meals",
                            icon = Icons.Default.Restaurant
                        ) {
                            day.meals.forEach { meal ->
                                ItineraryItemContent("${meal.time} - ${meal.type}")
                                meal.venue?.let { ItineraryItemContent("at $it") }
                                ItineraryItemContent("Cost: $${meal.cost}")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = primaryBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = primaryBlue
            )
        }
        Column(modifier = Modifier.padding(start = 28.dp)) {
            content()
        }
    }
}

@Composable
private fun ItineraryItemContent(text: String) {
    Text(
        text = text,
        color = Color(0xFF333333),
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = white,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Trip Notes",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = notes,
                onValueChange = onNotesChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("Add any notes or reminders for your trip...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = primaryBlue.copy(alpha = 0.05f),
                    unfocusedContainerColor = primaryBlue.copy(alpha = 0.05f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                minLines = 3
            )
        }
    }
}