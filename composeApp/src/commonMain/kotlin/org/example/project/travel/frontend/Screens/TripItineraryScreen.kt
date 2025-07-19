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
import androidx.compose.material.icons.filled.*
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
import com.arkivanov.decompose.ComponentContext
import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.model.dto.FlightDTO
import org.example.project.travel.frontend.navigation.RootComponent
import kotlin.random.Random
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.example.project.travel.frontEnd.model.TripRequestDTO
import org.example.project.travel.frontEnd.model.Meal
import org.example.project.travel.frontEnd.network.TripService
import org.example.project.travel.frontend.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.travel.frontEnd.model.TripActivity
import com.example.travel.network.TrainSearchResultDTO
import kotlinx.datetime.Clock

// Data classes remain unchanged
data class ItineraryDay(
    val date: String,
    val activities: List<TripActivity>,
    val transport: Transport?,
    val accommodation: Accommodation,
    val meals: List<Meal>
)

data class Transport(
    val type: String,
    val details: String,
    val time: String,
    val cost: Int
)

data class Accommodation(
    val name: String,
    val type: String,
    val checkIn: String,
    val checkOut: String,
    val cost: Int
)

private val primaryBlue = Color(23, 111, 243)
private val white = Color.White

interface TripItineraryScreenComponent : ComponentContext {
    val selectedFlight: FlightDTO?
    val selectedTrain: TrainSearchResultDTO?
    val selectedHotel: AccommodationDTO
    val selectedCityName: String
    val selectedCoach: String?
    val fare: Int?
    fun onNavigateToTransport()
    fun onNavigateToAccommodation()
    fun onNavigateToActivities()
    fun onNavigateToDining()
    fun onProceedToBooking(itinerary: List<ItineraryDay>, tripNotes: String)
    fun onGoBack()
}

class TripItineraryScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    override val selectedFlight: FlightDTO? = null,
    override val selectedTrain: TrainSearchResultDTO? = null,
    override val selectedHotel: AccommodationDTO,
    override val selectedCityName: String,
    override val selectedCoach: String? = null,
    override val fare: Int? = null,
    private val networkService: TripService // Make sure this is injected
) : TripItineraryScreenComponent, ComponentContext by componentContext {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onNavigateToTransport() { /*TODO*/ }
    override fun onNavigateToAccommodation() { /*TODO*/ }
    override fun onNavigateToActivities() { /*TODO*/ }
    override fun onNavigateToDining() { /*TODO*/ }
    override fun onGoBack() { rootComponent.pop() }

    suspend fun saveTripAndProceed(itinerary: List<ItineraryDay>, tripNotes: String) {
        val day = itinerary.first()
        val isTrainTrip = selectedTrain != null
        val flightCost = if (isTrainTrip) (fare ?: 0) else (selectedFlight?.price?.toInt() ?: 0)
        val hotelCost = selectedHotel.pricePerNight.toInt()
        val activitiesCost = day.activities.sumOf { it.cost }
        val mealsCost = day.meals.sumOf { it.cost }
        val transportationCost = day.transport?.cost ?: 0

        val costBreakdown = org.example.project.travel.frontEnd.model.TripCostBreakdown(
            flight = flightCost,
            hotel = hotelCost,
            activities = activitiesCost,
            meals = mealsCost,
            transportation = transportationCost
        )

        val tripData = if (isTrainTrip) {
            val train = selectedTrain!!
            org.example.project.travel.frontEnd.model.TripRequestDTO(
                flightId = "Train: ${train.train_no} - ${train.train_name}",
                cityName = selectedCityName,
                hotelName = selectedHotel.name,
                activities = day.activities,
                meals = day.meals,
                notes = tripNotes,
                costBreakdown = costBreakdown,
                hotelPrice = selectedHotel.pricePerNight.toInt(),
                flightPrice = 0,
                transportPrice = fare ?: 0
            )
        } else {
            org.example.project.travel.frontEnd.model.TripRequestDTO(
                flightId = selectedFlight?.airlineCode + "-" + selectedFlight?.flightNumber,
                cityName = selectedCityName,
                hotelName = selectedHotel.name,
                activities = day.activities,
                meals = day.meals,
                notes = tripNotes,
                costBreakdown = costBreakdown,
                hotelPrice = selectedHotel.pricePerNight.toInt(),
                flightPrice = selectedFlight?.price?.toInt() ?: 0,
                transportPrice = 0
            )
        }
        try {
            val tripId = networkService.saveTrip(tripData)
            // Gather trip details for confirmation screen
            val formattedDeparture = formatInstant(
                selectedFlight?.departure?.time
                    ?: selectedTrain?.from_departure_time?.let { parseTrainTimeToInstant(it) }
                    ?: Clock.System.now()
            )
            val formattedArrival = formatInstant(
                selectedFlight?.arrival?.time
                    ?: selectedTrain?.to_arrival_time?.let { parseTrainTimeToInstant(it) }
                    ?: Clock.System.now()
            )
            val formattedCheckIn = day.accommodation.checkIn // Already a string
            val formattedCheckOut = day.accommodation.checkOut // Already a string

            val destination = selectedCityName
            val dates = "${formattedDeparture.first} to ${formattedCheckOut}" // Example
            val transportDetails = if (isTrainTrip) {
                val train = selectedTrain!!
                "Train: ${train.train_no} - ${train.train_name}, ${train.from_station_name} to ${train.to_station_name}"
            } else {
                val flight = selectedFlight!!
                "${flight.airlineCode} ${flight.flightNumber}"
            }
            val hotelDetails = "${selectedHotel.name}, ${selectedHotel.pricePerNight} per night"
            val activities = day.activities.joinToString { it.name }
            val meals = day.meals.joinToString { it.type }
            val costBreakdownStr = "Flight/Train: ₹${flightCost}, Hotel: ₹${hotelCost}/night, Activities: ₹${activitiesCost}, Meals: ₹${mealsCost}, Transport: ₹${transportationCost}"
            val notesForSummary = tripNotes
            rootComponent.navigateTo(
                Screen.TripConfirmation(
                    destination = destination,
                    dates = dates,
                    flightDetails = transportDetails,
                    hotelDetails = hotelDetails,
                    activities = activities,
                    meals = meals,
                    costBreakdown = costBreakdownStr,
                    notes = notesForSummary
                )
            )
        } catch (e: Exception) {
            // Handle error (e.g., update state in UI)
        }
    }

    override fun onProceedToBooking(itinerary: List<ItineraryDay>, tripNotes: String) {
        coroutineScope.launch {
            saveTripAndProceed(itinerary, tripNotes)
        }
    }
}

@Composable
fun TripItineraryScreen(
    component: TripItineraryScreenComponent
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var tripNotes by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }

    // Sample data - Replace with ViewModel in production
    val itinerary = remember(component.selectedFlight, component.selectedHotel, component.selectedTrain) {
        val isTrainTrip = component.selectedTrain != null
        val arrivalTime = component.selectedFlight?.arrival?.time
            ?: component.selectedTrain?.to_arrival_time?.let { parseTrainTimeToInstant(it) }
            ?: Clock.System.now()
        val checkInTime = arrivalTime.plus(1, DateTimeUnit.HOUR)

        // Calculate checkout time: 11:00 AM on the day after check-in, in the system's local timezone
        val tz = TimeZone.currentSystemDefault()
        val checkOutInstant = checkInTime.plus(1, DateTimeUnit.DAY, tz)
        val checkOutLocalDateTime = checkOutInstant.toLocalDateTime(tz)
        val checkOutTime = LocalDateTime(
            year = checkOutLocalDateTime.year,
            monthNumber = checkOutLocalDateTime.monthNumber,
            dayOfMonth = checkOutLocalDateTime.dayOfMonth,
            hour = 11,
            minute = 0
        ).toInstant(tz)

        val formattedDeparture = formatInstant(
            component.selectedFlight?.departure?.time
                ?: component.selectedTrain?.from_departure_time?.let { parseTrainTimeToInstant(it) }
                ?: Clock.System.now()
        )
        val formattedArrival = formatInstant(arrivalTime)
        val formattedCheckIn = formatInstant(checkInTime)
        val formattedCheckOut = formatInstant(checkOutTime)

        val transportType = if (isTrainTrip) "Train" else "Flight"
        val transportDetails = if (isTrainTrip) {
            val train = component.selectedTrain!!
            "Train: ${train.train_no} - ${train.train_name}\n" +
            "From: ${train.from_station_name} (${train.from_station_code})\n" +
            "To: ${train.to_station_name} (${train.to_station_code})" +
            (if (component.selectedCoach != null) "\nCoach: ${component.selectedCoach}" else "") 
//            (if (component.fare != null) "\nFare: ₹${component.fare}" else "")
        } else {
            val flight = component.selectedFlight!!
            "Flight: ${flight.airlineCode} ${flight.flightNumber}"
        }
        val transportCost = if (isTrainTrip) (component.fare?.toInt() ?: 0) else (component.selectedFlight?.price?.toInt() ?: 0)

        listOf(
            ItineraryDay(
                date = "Trip Summary",
                activities = listOf(
                    TripActivity("09:00", "Breakfast", "Enjoy breakfast at the hotel.", 0),
                    TripActivity("10:00", "City Tour", "Explore the city's main attractions.", 0),
                    TripActivity("13:00", "Lunch", "Have lunch at a local restaurant.", 0),
                    TripActivity("15:00", "Local Sightseeing", "Visit the interesting places.", 0),
                    TripActivity("19:00", "Dinner", "Enjoy a nice dinner.", 0)
                ),
                transport = Transport(
                    type = transportType,
                    details = transportDetails,
                    time = "Departs: ${formattedDeparture.first}, ${formattedDeparture.second}\nArrives: ${formattedArrival.first}, ${formattedArrival.second}",
                    cost = transportCost
                ),
                accommodation = Accommodation(
                    name = component.selectedHotel.name,
                    type = "Hotel",
                    checkIn = "${formattedCheckIn.first}, ${formattedCheckIn.second}",
                    checkOut = "${formattedCheckOut.first}, ${formattedCheckOut.second}",
                    cost = component.selectedHotel.pricePerNight.toInt()
                ),
                meals = listOf(
                    Meal("Breakfast", "Hotel", "09:00", (500..1000).random()),
                    Meal("Lunch", "Local Restaurant", "13:00", (500..1000).random()),
                    Meal("Dinner", "Local Restaurant", "19:00", (500..1000).random())
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
                    onNavigateToTransport = component::onNavigateToTransport,
                    onNavigateToAccommodation = component::onNavigateToAccommodation,
                    onNavigateToActivities = component::onNavigateToActivities,
                    onNavigateToDining = component::onNavigateToDining
                )
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
                        component.onProceedToBooking(itinerary, tripNotes)
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

private fun formatInstant(instant: Instant): Pair<String, String> {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val date = "${localDateTime.dayOfMonth} ${localDateTime.month.name.take(3)} ${localDateTime.year}"
    val time = "%02d:%02d".format(localDateTime.hour, localDateTime.minute)
    return Pair(date, time)
}

fun parseTrainTimeToInstant(time: String): Instant {
    // Assumes time is in format "HH:mm:ss" and today as date
    val today = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    val dateTimeStr = "${today}T${time}"
    return try {
        kotlinx.datetime.LocalDateTime.parse(dateTimeStr).toInstant(kotlinx.datetime.TimeZone.currentSystemDefault())
    } catch (e: Exception) {
        Clock.System.now()
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
                            ItineraryItemContent("Cost: ₹${transport.cost}")
                        }
                    }

                    ItemSection(
                        title = "Accommodation",
                        icon = Icons.Default.Hotel
                    ) {
                        ItineraryItemContent(day.accommodation.name)
                        ItineraryItemContent("Check-in: ${day.accommodation.checkIn}")
                        ItineraryItemContent("Check-out: ${day.accommodation.checkOut}")
                        ItineraryItemContent("Cost: ₹${day.accommodation.cost}/night")
                    }

                    if (day.activities.isNotEmpty()) {
                        ItemSection(
                            title = "Activities",
                            icon = Icons.Default.LocalActivity
                        ) {
                            day.activities.forEach { activity ->
                                ItineraryItemContent("${activity.time} - ${activity.name}")
                                if (activity.cost > 0) {
                                    ItineraryItemContent("Cost: ₹${activity.cost}")
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
                                ItineraryItemContent("Cost: ₹${meal.cost}")
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