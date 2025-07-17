package org.example.project.travel.frontend.Screens.Transportation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.ComponentContext
import kotlinx.datetime.*
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.travel.frontEnd.utils.formatDate
import org.example.project.travel.frontEnd.utils.isLeapYear
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Directions
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travel.network.fetchTrainStations
import com.example.travel.network.TrainStationDTO
import kotlinx.coroutines.launch


interface TrainSearchScreenComponent {
    fun navigateTo(screen: Screen)
    fun replaceAll(screen: Screen)
    fun pop()
}

class TrainSearchScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : TrainSearchScreenComponent, ComponentContext by componentContext {
    override fun navigateTo(screen: Screen) {
        rootComponent.navigateTo(screen)
    }
    override fun replaceAll(screen: Screen) {
        rootComponent.replaceAll(screen)
    }
    override fun pop() {
        rootComponent.pop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainSearchScreen(
    component: TrainSearchScreenComponent
) {
    var fromStation by remember { mutableStateOf("PNBE - Patna - All stations") }
    var toStation by remember { mutableStateOf("JBP - Jabalpur") }
    var selectedDate by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Trains") }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val dayAfter = today.plus(2, DateTimeUnit.DAY)
    var showComingSoonDialog by remember { mutableStateOf(false) }
    var showFromDialog by remember { mutableStateOf(false) }
    var showToDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var stationSearchQuery by remember { mutableStateOf("") }
    var stationResults by remember { mutableStateOf<List<TrainStationDTO>>(emptyList()) }
    var isLoadingStations by remember { mutableStateOf(false) }
    var stationDialogType by remember { mutableStateOf("") } // "from" or "to"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color.White) // Main background is white
    ) {
        // Top Section with Heading and Tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(23, 111, 243).copy(alpha = 0.05f),
                            Color.White
                        )
                    )
                )
        ) {
            // Heading: Select Transportation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF176FF3)) // Top bar is #176FF3
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Transportation",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Top Navigation Bar (Tabs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem("Flights", Icons.Default.Flight, selectedTab) {
                    component.replaceAll(Screen.FlightSearch)
                }
                TabItem("Bus", Icons.Default.DirectionsBus, selectedTab) {
                    showComingSoonDialog = true
                }
                TabItem("Trains", Icons.Default.Train, selectedTab) { selectedTab = "Trains" }
                TabItem("Private Cab", Icons.Default.DirectionsCar, selectedTab) {
                    showComingSoonDialog = true
                }
            }
        }
        Divider(color = Color.LightGray.copy(alpha = 0.5f))
        // Main content area (white background)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FROM
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        showFromDialog = true
                        stationDialogType = "from"
                        stationSearchQuery = ""
                        stationResults = emptyList()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Train, contentDescription = null, tint = Color(0xFF176FF3))
                Spacer(modifier = Modifier.width(12.dp))
                Text(fromStation, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = Color.Black)
            }
            // SWAP
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = fromStation
                        fromStation = toStation
                        toStation = temp
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFF0F0F0), shape = CircleShape)
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = Color(0xFF176FF3))
                }
            }
            // TO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        showToDialog = true
                        stationDialogType = "to"
                        stationSearchQuery = ""
                        stationResults = emptyList()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Train, contentDescription = null, tint = Color(0xFF176FF3))
                Spacer(modifier = Modifier.width(12.dp))
                Text(toStation, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = Color.Black)
            }
            // DATE
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showDatePicker = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF176FF3))
                Spacer(modifier = Modifier.width(12.dp))
                Text(formatDate(selectedDate), fontWeight = FontWeight.Medium, fontSize = 17.sp, color = Color.Black)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { selectedDate = today },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Today", color = if (selectedDate == today) Color(0xFF176FF3) else Color.Gray)
                }
                Button(
                    onClick = { selectedDate = tomorrow },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Tomorrow", color = if (selectedDate == tomorrow) Color(0xFF176FF3) else Color.Gray)
                }
                Button(
                    onClick = { selectedDate = dayAfter },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null
                ) {
                    Text("Day After", color = if (selectedDate == dayAfter) Color(0xFF176FF3) else Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // SEARCH TRAINS BUTTON
            Button(
                onClick = {
                    val fromCode = fromStation.substringBefore(" - ")
                    val toCode = toStation.substringBefore(" - ")
                    component.navigateTo(Screen.TrainDetails(fromCode, toCode))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF176FF3)) // Accent color
            ) {
                Text("SEARCH TRAINS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

    // Date Picker Dialog (copied and adapted from FlightSearchScreen)
    if (showDatePicker) {
        val currentMoment = Clock.System.now()
        val today = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
        var selectedDateLocal by remember { mutableStateOf(selectedDate) }

        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = {
                Text(
                    "Select Date",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF176FF3)
                )
            },
            text = {
                Column {
                    // Month and Year selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                selectedDateLocal = selectedDateLocal.minus(1, DateTimeUnit.MONTH)
                            }
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Previous month", tint = Color(0xFF176FF3))
                        }
                        Text(
                            "${selectedDateLocal.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDateLocal.year}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF176FF3)
                        )
                        IconButton(
                            onClick = {
                                selectedDateLocal = selectedDateLocal.plus(1, DateTimeUnit.MONTH)
                            }
                        ) {
                            Icon(Icons.Default.ChevronRight, "Next month", tint = Color(0xFF176FF3))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Days of week header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF176FF3)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Calendar grid
                    val firstDayOfMonth = LocalDate(selectedDateLocal.year, selectedDateLocal.monthNumber, 1)
                    val daysInMonth = selectedDateLocal.month.length(isLeapYear(selectedDateLocal.year))
                    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // Sunday start
                    Column {
                        var dayCounter = 1
                        repeat(6) { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                repeat(7) { dayOfWeek ->
                                    val dayIndex = week * 7 + dayOfWeek
                                    val showDay = dayIndex >= firstDayOfWeek && dayCounter <= daysInMonth
                                    if (showDay) {
                                        val currentDate = LocalDate(
                                            selectedDateLocal.year,
                                            selectedDateLocal.monthNumber,
                                            dayCounter
                                        )
                                        val isSelected = currentDate == selectedDateLocal
                                        val isToday = currentDate == today
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(2.dp)
                                                .background(
                                                    color = if (isSelected) Color(0xFF176FF3) else Color.Transparent,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .clickable(enabled = currentDate >= today) {
                                                    selectedDateLocal = currentDate
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = dayCounter.toString(),
                                                color = when {
                                                    isSelected -> Color.White
                                                    isToday -> Color(0xFF176FF3)
                                                    currentDate < today -> Color(0xFF176FF3).copy(alpha = 0.5f)
                                                    else -> Color(0xFF176FF3)
                                                },
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = when {
                                                    isSelected || isToday -> FontWeight.Bold
                                                    else -> FontWeight.Normal
                                                }
                                            )
                                        }
                                        dayCounter++
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = selectedDateLocal
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = Color(0xFF176FF3))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color(0xFF176FF3))
                }
            }
        )
    }

    // Station Search Dialog
    if (showFromDialog || showToDialog) {
        AlertDialog(
            onDismissRequest = {
                showFromDialog = false
                showToDialog = false
            },
            title = { Text("Select Station", color = Color(0xFF176FF3)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = stationSearchQuery,
                        onValueChange = {
                            stationSearchQuery = it
                            if (it.length >= 2) {
                                isLoadingStations = true
                                coroutineScope.launch {
                                    try {
                                        stationResults = fetchTrainStations(it)
                                    } catch (e: Exception) {
                                        stationResults = emptyList()
                                    }
                                    isLoadingStations = false
                                }
                            } else {
                                stationResults = emptyList()
                            }
                        },
                        label = { Text("Search station name or code") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (isLoadingStations) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Column(modifier = Modifier.heightIn(max = 300.dp)) {
                            stationResults.forEach { station ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (stationDialogType == "from") {
                                                fromStation = "${station.station_code} - ${station.station_name}"
                                            } else {
                                                toStation = "${station.station_code} - ${station.station_name}"
                                            }
                                            showFromDialog = false
                                            showToDialog = false
                                        }
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Train, contentDescription = null, tint = Color(0xFF176FF3))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${station.station_code} - ${station.station_name}", color = Color(0xFF176FF3), fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showFromDialog = false
                    showToDialog = false
                }) {
                    Text("Cancel", color = Color(0xFF176FF3))
                }
            }
        )
    }

    if (showComingSoonDialog) {
        AlertDialog(
            onDismissRequest = { showComingSoonDialog = false },
            title = { Text("Coming Soon!", color = Color(0xFF176FF3)) },
            text = { Text("This feature will be available soon.", color = Color(0xFF176FF3)) },
            confirmButton = {
                TextButton(onClick = { showComingSoonDialog = false }) {
                    Text("OK", color = Color(0xFF176FF3))
                }
            }
        )
    }
}

@Composable
fun TabItem(
    title: String,
    icon: ImageVector,
    selectedTab: String,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (selectedTab == title) 1f else 0.6f,
        label = "Tab Alpha Animation"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(23, 111, 243).copy(alpha = alpha),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            color = Color(0xFF424242).copy(alpha = alpha),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(72.dp)
        )
    }
}

@Composable
fun TrainDetailsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Train Details Screen", color = Color(0xFF176FF3), fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
}