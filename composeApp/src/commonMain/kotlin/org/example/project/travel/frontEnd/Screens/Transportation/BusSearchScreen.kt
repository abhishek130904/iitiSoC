package org.example.project.travel.frontend.Screens.Transportation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen

interface BusSearchScreenComponent {
    fun navigateTo(screen: Screen)
    fun pop()
}

class BusSearchScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : BusSearchScreenComponent, ComponentContext by componentContext {
    override fun navigateTo(screen: Screen) {
        rootComponent.navigateTo(screen)
    }

    override fun pop() {
        rootComponent.pop()
    }
}

@Composable
fun BusSearchScreen(
    component: BusSearchScreenComponent
) {
    Text("Bus Search Screen")
}



//package org.example.project.travel.frontend.Screens.Transportation
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.arkivanov.decompose.ComponentContext
//import org.example.project.travel.frontend.viewModel.BusSearchViewModel
//import org.example.project.travel.frontend.viewModel.BusType
//import kotlinx.datetime.*
//import org.example.project.travel.frontend.navigation.RootComponent
//import org.example.project.travel.frontend.navigation.Screen
//
//interface BusSearchScreenComponent {
//    fun navigateTo(screen: Screen)
//    fun pop()
//}
//
//class BusSearchScreenComponentImpl(
//    componentContext: ComponentContext,
//    private val rootComponent: RootComponent
//) : BusSearchScreenComponent, ComponentContext by componentContext {
//    override fun navigateTo(screen: Screen) {
//        rootComponent.navigateTo(screen)
//    }
//
//    override fun pop() {
//        rootComponent.pop()
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BusSearchScreen(
//    component: BusSearchScreenComponent
//) {
//    val busSearchViewModel = remember { BusSearchViewModel() }
//    val state by busSearchViewModel.state.collectAsState()
//    var selectedTab by remember { mutableStateOf("Bus") }
//    var showDatePicker by remember { mutableStateOf(false) }
//    var showCityPicker by remember { mutableStateOf<String?>(null) }
//    var showPassengerDialog by remember { mutableStateOf(false) }
//    var showBusTypeDialog by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(red = 23, green = 111, blue = 243))
//                .statusBarsPadding()
//                .padding(vertical = 20.dp)
//        ) {
//            Text(
//                text = "Select Transportation",
//                color = Color.White,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        // Top Navigation Bar
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            TabItem("Flights", Icons.Default.Flight, selectedTab) {
//                selectedTab = "Flights"
////                onNavigateToFlights()
//            }
//            TabItem("Bus", Icons.Default.DirectionsBus, selectedTab) {
//                selectedTab = "Bus"
//            }
//            TabItem("Trains", Icons.Default.Train, selectedTab) {
//                selectedTab = "Trains"
////                onNavigateToTrains()
//            }
//            TabItem("Private Cab", Icons.Default.DirectionsCar, selectedTab) {
//                selectedTab = "Private Cab"
////                onNavigateToPrivateCab()
//            }
//        }
//
//        Divider(color = Color.LightGray.copy(alpha = 0.5f))
//
//        // Main Content
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp),
//            contentPadding = PaddingValues(vertical = 16.dp)
//        ) {
//            // From Field
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.LocationOn,
//                        contentDescription = "From",
//                        tint = Color(23, 111, 243),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("From", color = Color(0xFF424242), fontSize = 14.sp)
//                }
//            }
//
//            // From City Box
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = 40.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { showCityPicker = "from" }
//                            .padding(start = 28.dp)
//                    ) {
//                        Text(
//                            state.fromCityName,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//
//            // Swap Button
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    IconButton(
//                        onClick = { busSearchViewModel.swapCities() }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.SwapVert,
//                            contentDescription = "Swap cities",
//                            tint = Color(23, 111, 243),
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//            }
//
//            // To Field
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.LocationOn,
//                        contentDescription = "To",
//                        tint = Color(23, 111, 243),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("To", color = Color(0xFF424242), fontSize = 14.sp)
//                }
//            }
//
//            // To City Box
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = 40.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { showCityPicker = "to" }
//                            .padding(start = 28.dp)
//                    ) {
//                        Text(
//                            state.toCityName,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//
//            // Date Selection
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { showDatePicker = true }
//                        .padding(vertical = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CalendarToday,
//                        contentDescription = "Date",
//                        tint = Color(23, 111, 243),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = state.selectedDate.ifEmpty { "Select Date" },
//                        color = if (state.selectedDate.isEmpty()) Color(0xFF424242) else Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//
//            // Passenger Count
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { showPassengerDialog = true }
//                        .padding(vertical = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Passengers",
//                        tint = Color(23, 111, 243),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "${state.passengerCount} Passenger${if (state.passengerCount > 1) "s" else ""}",
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//
//            // Bus Type Selection
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { showBusTypeDialog = true }
//                        .padding(vertical = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.AirlineSeatReclineNormal,
//                        contentDescription = "Bus Type",
//                        tint = Color(23, 111, 243),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = state.busType.displayName(),
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//
//            // Search Button
//            item {
//                Button(
//                    onClick = onSearch,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(23, 111, 243)
//                    ),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text(
//                        "Search Buses",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            }
//        }
//    }
//
//    // City Selection Dialog
//    if (showCityPicker != null) {
//        AlertDialog(
//            onDismissRequest = { showCityPicker = null },
//            title = { Text("Select ${if (showCityPicker == "from") "Departure" else "Destination"} City") },
//            text = {
//                LazyColumn {
//                    items(busSearchViewModel.popularStations) { (code, city, station) ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    if (showCityPicker == "from") {
//                                        busSearchViewModel.updateFromCity(code, city, station)
//                                    } else {
//                                        busSearchViewModel.updateToCity(code, city, station)
//                                    }
//                                    showCityPicker = null
//                                }
//                                .padding(vertical = 12.dp)
//                        ) {
//                            Text(
//                                city,
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Medium,
//                                color = Color(0xFF424242)
//                            )
//                        }
//                    }
//                }
//            },
//            confirmButton = {},
//            dismissButton = {
//                TextButton(onClick = { showCityPicker = null }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    // Bus Type Selection Dialog
//    if (showBusTypeDialog) {
//        AlertDialog(
//            onDismissRequest = { showBusTypeDialog = false },
//            title = { Text("Select Bus Type") },
//            text = {
//                Column {
//                    BusType.values().forEach { busType ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    busSearchViewModel.updateBusType(busType)
//                                    showBusTypeDialog = false
//                                }
//                                .padding(vertical = 12.dp)
//                        ) {
//                            Text(busType.displayName())
//                        }
//                    }
//                }
//            },
//            confirmButton = {},
//            dismissButton = {
//                TextButton(onClick = { showBusTypeDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    // Passenger Count Dialog
//    if (showPassengerDialog) {
//        var passengerCount by remember { mutableStateOf(state.passengerCount) }
//
//        AlertDialog(
//            onDismissRequest = { showPassengerDialog = false },
//            title = { Text("Select Number of Passengers") },
//            text = {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(
//                        onClick = { if (passengerCount > 1) passengerCount-- },
//                        enabled = passengerCount > 1
//                    ) {
//                        Icon(Icons.Default.Remove, "Decrease")
//                    }
//                    Text(
//                        text = passengerCount.toString(),
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    IconButton(
//                        onClick = { if (passengerCount < 6) passengerCount++ },
//                        enabled = passengerCount < 6
//                    ) {
//                        Icon(Icons.Default.Add, "Increase")
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        busSearchViewModel.updatePassengerCount(passengerCount)
//                        showPassengerDialog = false
//                    }
//                ) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showPassengerDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    // Date Picker Dialog
//    if (showDatePicker) {
//        val currentMoment = Clock.System.now()
//        val today = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
//        var selectedDateLocal by remember { mutableStateOf(today) }
//
//        AlertDialog(
//            onDismissRequest = { showDatePicker = false },
//            title = {
//                Text(
//                    "Select Date",
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.SemiBold
//                )
//            },
//            text = {
//                Column {
//                    // Month and Year selector
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconButton(
//                            onClick = {
//                                selectedDateLocal = selectedDateLocal.minus(1, DateTimeUnit.MONTH)
//                            }
//                        ) {
//                            Icon(Icons.Default.ChevronLeft, "Previous month", tint = Color(23, 111, 243))
//                        }
//
//                        Text(
//                            "${selectedDateLocal.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDateLocal.year}",
//                            style = MaterialTheme.typography.titleMedium,
//                            color = Color.Black
//                        )
//
//                        IconButton(
//                            onClick = {
//                                selectedDateLocal = selectedDateLocal.plus(1, DateTimeUnit.MONTH)
//                            }
//                        ) {
//                            Icon(Icons.Default.ChevronRight, "Next month", tint = Color(23, 111, 243))
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Days of week header
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
//                            Text(
//                                text = day,
//                                modifier = Modifier.weight(1f),
//                                textAlign = TextAlign.Center,
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color(0xFF424242)
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Calendar grid
//                    val firstDayOfMonth = LocalDate(selectedDateLocal.year, selectedDateLocal.monthNumber, 1)
//                    val daysInMonth = selectedDateLocal.month.length(isLeapYear(selectedDateLocal.year))
//                    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // Sunday start
//
//                    Column {
//                        var dayCounter = 1
//                        repeat(6) { week ->
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                repeat(7) { dayOfWeek ->
//                                    val dayIndex = week * 7 + dayOfWeek
//                                    val showDay = dayIndex >= firstDayOfWeek && dayCounter <= daysInMonth
//
//                                    if (showDay) {
//                                        val currentDate = LocalDate(
//                                            selectedDateLocal.year,
//                                            selectedDateLocal.monthNumber,
//                                            dayCounter
//                                        )
//                                        val isSelected = currentDate == selectedDateLocal
//                                        val isToday = currentDate == today
//
//                                        Box(
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .aspectRatio(1f)
//                                                .padding(2.dp)
//                                                .background(
//                                                    color = if (isSelected) Color(23, 111, 243) else Color.Transparent,
//                                                    shape = RoundedCornerShape(50)
//                                                )
//                                                .clickable(
//                                                    enabled = currentDate >= today
//                                                ) {
//                                                    selectedDateLocal = currentDate
//                                                },
//                                            contentAlignment = Alignment.Center
//                                        ) {
//                                            Text(
//                                                text = dayCounter.toString(),
//                                                color = when {
//                                                    isSelected -> Color.White
//                                                    isToday -> Color(23, 111, 243)
//                                                    currentDate < today -> Color(0xFF424242).copy(alpha = 0.5f)
//                                                    else -> Color.Black
//                                                },
//                                                style = MaterialTheme.typography.bodyMedium,
//                                                fontWeight = when {
//                                                    isSelected || isToday -> FontWeight.Bold
//                                                    else -> FontWeight.Normal
//                                                }
//                                            )
//                                        }
//                                        dayCounter++
//                                    } else {
//                                        Box(
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .aspectRatio(1f)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        busSearchViewModel.updateDate(formatDate(selectedDateLocal))
//                        showDatePicker = false
//                    }
//                ) {
//                    Text("OK", color = Color(23, 111, 243))
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) {
//                    Text("Cancel", color = Color(23, 111, 243))
//                }
//            }
//        )
//    }
//}
//
//@Composable
//private fun TabItem(
//    title: String,
//    icon: ImageVector,
//    selectedTab: String,
//    onClick: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .clickable(onClick = onClick)
//            .padding(8.dp)
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = title,
//            tint = if (selectedTab == title) Color(23, 111, 243) else Color.Gray,
//            modifier = Modifier.size(24.dp)
//        )
//        Text(
//            text = title,
//            color = if (selectedTab == title) Color(23, 111, 243) else Color.Gray,
//            fontSize = 12.sp,
//            modifier = Modifier.padding(top = 4.dp)
//        )
//    }
//}
//
//private fun isLeapYear(year: Int): Boolean {
//    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
//}
//
//private fun formatDate(date: LocalDate): String {
//    val dayOfWeek = date.dayOfWeek.name.take(3)
//    val day = date.dayOfMonth.toString().padStart(2, '0')
//    val month = date.month.name.take(3)
//    val year = date.year.toString().takeLast(2)
//    return "$dayOfWeek, $day $month $year"
//}