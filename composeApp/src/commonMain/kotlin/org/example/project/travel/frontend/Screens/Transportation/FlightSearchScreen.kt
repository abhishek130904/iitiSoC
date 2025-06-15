package org.example.project.travel.frontend.Screens.Transportation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import androidx.compose.ui.graphics.Brush
import com.arkivanov.decompose.ComponentContext
import com.example.travel.model.dto.FlightDTO
import com.example.travel.viewmodel.CityViewModel
import com.example.travel.viewmodel.FlightViewModel
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import com.arkivanov.decompose.extensions.compose.subscribeAsState

interface FlightSearchScreenComponent {
//    val cityViewModel: CityViewModel
    val flightViewModel: FlightViewModel
//    val flights: List<FlightDTO>
    fun navigateTo(screen: Screen)
//    fun pop()
}

class FlightSearchScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : FlightSearchScreenComponent, ComponentContext by componentContext {
//    override val cityViewModel = CityViewModel()
override val flightViewModel: FlightViewModel = rootComponent.flightViewModel

//    override val flights: List<FlightDTO>
//        get() = flightViewModel.flights.value

    override fun navigateTo(screen: Screen) {

        rootComponent.navigateTo(screen)
    }

//    override fun pop() {
//        rootComponent.pop()
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    component: FlightSearchScreenComponent
) {
    val cityViewModel = remember { CityViewModel() }
    val flightViewModel = remember { FlightViewModel() }
    var selectedTab by remember { mutableStateOf("Flights") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCityPicker by remember { mutableStateOf<String?>(null) }
    var showPassengerDialog by remember { mutableStateOf(false) }
    var showCabinClassDialog by remember { mutableStateOf(false) }

    val cities by cityViewModel.cities.collectAsState()
    val flights by flightViewModel.flights.collectAsState()
    val isFlightsLoading by flightViewModel.isFlightsLoading.collectAsState()
    val flightsError by flightViewModel.flightsError.collectAsState()
    val citiesError by cityViewModel.citiesError.collectAsState()

    val searchState by flightViewModel.searchState.collectAsState()
    val safeSearchState = searchState

    // Log the initial search state
    println("FlightSearchScreen: Initial searchState - fromCity=${searchState.fromCity}, toCity=${searchState.toCity}, selectedDate=${searchState.selectedDate}, adults=${searchState.adultCount}, children=${searchState.childCount}, infants=${searchState.infantCount}, cabinClass=${searchState.cabinClass}")

    // Log the flights state whenever it changes
    println("FlightSearchScreen: Flights state - flights=$flights, isLoading=$isFlightsLoading, error=$flightsError")

    DisposableEffect(Unit) {
        println("FlightSearchScreen: DisposableEffect - onStart")
        onDispose {
            println("FlightSearchScreen: DisposableEffect - onDispose")
        }
    }

    LaunchedEffect(flights) {
        if (flights.isNotEmpty()) {
            println("FlightSearchScreen: Flights loaded - flights count=${flights.size}")
            println("FlightSearchScreen: Navigating to flight_detail with ${flights.size} flights")
            val screen: Screen = Screen.FlightDetail(flights) // Explicitly declare the type
            component.navigateTo(screen)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color.White)
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
                    .background(Color(0xFF176FF3))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Transportation",
                    fontSize = 22.sp, // Reduced font size
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Space between heading and tabs

            // Top Navigation Bar (Tabs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem("Flights", Icons.Default.Flight, selectedTab) { selectedTab = "Flights" }
                TabItem("Bus", Icons.Default.DirectionsBus, selectedTab) {
                    selectedTab = "Bus"
                    println("FlightSearchScreen: Bus tab clicked")
                    // component.navigateTo(Screen.BusSearch)
                }
                TabItem("Trains", Icons.Default.Train, selectedTab) {
                    selectedTab = "Trains"
                    println("FlightSearchScreen: Trains tab clicked")
                    // component.navigateTo(Screen.TrainSearch)
                }
                TabItem("Private Cab", Icons.Default.DirectionsCar, selectedTab) {
                    selectedTab = "Private Cab"
                    println("FlightSearchScreen: Private Cab tab clicked")
                    // component.navigateTo(Screen.PrivateCabSearch)
                }
            }
        }

        Divider(color = Color.LightGray.copy(alpha = 0.5f))

        // Main Content in LazyColumn
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // From Field
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "From",
                            tint = Color(23, 111, 243),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("From", color = Color(0xFF424242), fontSize = 14.sp)
                    }
                }

                // From City Box
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showCityPicker = "from"
                                    println("FlightSearchScreen: From city clicked, opening city picker")
                                }
                                .padding(start = 28.dp)
                        ) {
                            Text(
                                searchState.fromCity,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(searchState.fromCityName, color = Color(0xFF424242))
                            Text(
                                searchState.fromAirportName,
                                color = Color(0xFF424242),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Swap Button
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                flightViewModel.swapCities()
                                println("FlightSearchScreen: Swap cities clicked, new fromCity=${searchState.fromCity}, toCity=${searchState.toCity}")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap cities",
                                tint = Color(23, 111, 243),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // To Field
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "To",
                            tint = Color(23, 111, 243),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("To", color = Color(0xFF424242), fontSize = 14.sp)
                    }
                }

                // To City Box
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showCityPicker = "to"
                                    println("FlightSearchScreen: To city clicked, opening city picker")
                                }
                                .padding(start = 28.dp)
                        ) {
                            Text(
                                searchState.toCity,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(searchState.toCityName, color = Color(0xFF424242))
                            Text(
                                searchState.toAirportName,
                                color = Color(0xFF424242),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Date Selection
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color(23, 111, 243),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier.clickable {
                                showDatePicker = true
                                println("FlightSearchScreen: Departure date clicked, opening date picker")
                            }
                        ) {
                            Text("Departure Date", color = Color(0xFF424242), fontSize = 14.sp)
                            Text(
                                searchState.selectedDate.ifEmpty { formatDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                // Travelers & Class
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp
                    ) {
                        Column {
                            // Passenger Selection
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showPassengerDialog = true
                                        println("FlightSearchScreen: Passenger selection clicked, opening passenger dialog")
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Passengers",
                                    tint = Color(23, 111, 243),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Passengers",
                                        color = Color(0xFF424242),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        buildString {
                                            append("${searchState.adultCount} Adult")
                                            if (searchState.adultCount > 1) append("s")
                                            if (searchState.childCount > 0) {
                                                append(", ${searchState.childCount} Child")
                                                if (searchState.childCount > 1) append("ren")
                                            }
                                            if (searchState.infantCount > 0) {
                                                append(", $searchState.infantCount} Infant")
                                                if (searchState.infantCount > 1) append("s")
                                            }
                                        },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                }
                            }

                            Divider(color = Color.LightGray.copy(alpha = 0.5f))

                            // Cabin Class Selection
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showCabinClassDialog = true
                                        println("FlightSearchScreen: Cabin class selection clicked, opening cabin class dialog")
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AirlineSeatReclineNormal,
                                    contentDescription = "Cabin Class",
                                    tint = Color(23, 111, 243),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Cabin Class",
                                        color = Color(0xFF424242),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        searchState.cabinClass.displayName(),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                // Flight Search Results
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    if (isFlightsLoading || flights.isNotEmpty() || flightsError != null) {
//                        Surface(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp),
//                            shape = RoundedCornerShape(8.dp),
//                            shadowElevation = 2.dp
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            ) {
//                                Text(
//                                    "Flight Results",
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color.Black,
//                                    modifier = Modifier.padding(bottom = 8.dp)
//                                )
//                                when {
//                                    isFlightsLoading -> {
//                                        CircularProgressIndicator(
//                                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                                            color = Color(23, 111, 243)
//                                        )
//                                    }
//                                    flightsError != null -> {
//                                        Text(
//                                            text = flightsError ?: "Unknown error",
//                                            color = Color.Red,
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(16.dp),
//                                            textAlign = TextAlign.Center
//                                        )
//                                    }
//                                    flights.isEmpty() -> {
//                                        Text(
//                                            "No flights found.",
//                                            color = Color(0xFF424242),
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(16.dp),
//                                            textAlign = TextAlign.Center
//                                        )
//                                    }
//                                    else -> {
//                                        LazyColumn(
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .heightIn(max = 300.dp)
//                                        ) {
//                                            items(flights) { flight ->
//                                                FlightItem(
//                                                    flight = flight,
//                                                    cabinClass = safeSearchState.cabinClass.displayName(),
//                                                    totalPassengers =safeSearchState.adultCount + safeSearchState.childCount + safeSearchState.infantCount,
//                                                    onClick = {
//                                                        // We'll add navigation to FlightDetailScreen here
//                                                    }
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
                // Spacer for bottom padding
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Search Button - Floating at the bottom
            Button(
                onClick = {
                    val date = safeSearchState.selectedDate.ifEmpty {
                        formatDate(Clock.System.now().toLocalDateTime(TimeZone.of("Asia/Kolkata")).date)
                    }
                    // Log the parameters before calling searchFlights
                    println("FlightSearchScreen: Search button clicked - fromCity=${safeSearchState.fromCity}, toCity=${safeSearchState.toCity}, date=$date, adults=${safeSearchState.adultCount}, children=${safeSearchState.childCount}, infants=${safeSearchState.infantCount}, cabinClass=${safeSearchState.cabinClass}")
                    flightViewModel.searchFlights(
                        fromCity = safeSearchState.fromCity,
                        toCity = safeSearchState.toCity,
                        date = formatDateForApi(date),
                        adults = safeSearchState.adultCount,
                        children = safeSearchState.childCount,
                        infants = safeSearchState.infantCount,
                        cabinClass = safeSearchState.cabinClass.name
                    )
                    println("FlightSearchScreen: searchFlights called")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(23, 111, 243),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "Search Flights",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // City Selection Dialog
    if (showCityPicker != null) {
        var searchQuery by remember { mutableStateOf("") }
        val cities by cityViewModel.cities.collectAsState()
        val isLoading by cityViewModel.isCitiesLoading.collectAsState()
        val error by cityViewModel.citiesError.collectAsState()

        // Log when city picker dialog opens
        println("FlightSearchScreen: City picker dialog opened for ${showCityPicker}")

        AlertDialog(
            onDismissRequest = {
                showCityPicker = null
                println("FlightSearchScreen: City picker dialog dismissed")
            },
            title = { Text("Select ${if (showCityPicker == "from") "Departure" else "Destination"} City") },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            println("FlightSearchScreen: City search query updated - query=$searchQuery")
                        },
                        label = { Text("Search City") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    )
                    when {
                        isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            println("FlightSearchScreen: City picker - cities are loading")
                        }
                        error != null -> {
                            Text(
                                text = error ?: "Error",
                                color = Color.Red,
                                modifier = Modifier
                                    .clickable {
                                        cityViewModel.retryFetchCities()
                                        println("FlightSearchScreen: City picker - retryFetchCities clicked")
                                    }
                                    .align(Alignment.CenterHorizontally)
                            )
                            println("FlightSearchScreen: City picker - error=$error")
                        }
                        else -> {
                            if (searchQuery.isEmpty()) {
                                // Show popular airports
                                Column {
                                    Text(
                                        "Popular Airports",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    LazyColumn(
                                        modifier = Modifier.heightIn(max = 300.dp)
                                    ) {
                                        items(cityViewModel.popularAirports) { (code, nameAndAirport) ->
                                            val (name, airport) = nameAndAirport
                                            CityOption(
                                                code = code,
                                                name = name,
                                                airportName = airport,
                                                onSelect = { c, n, a ->
                                                    if (showCityPicker == "from") {
                                                        flightViewModel.updateFromCity(c, n, a)
                                                        println("FlightSearchScreen: Selected departure city - code=$c, name=$n, airport=$a")
                                                    } else {
                                                        flightViewModel.updateToCity(c, n, a)
                                                        println("FlightSearchScreen: Selected destination city - code=$c, name=$n, airport=$a")
                                                    }
                                                    showCityPicker = null
                                                    searchQuery = ""
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Show filtered cities
                                val filteredCities = cities.filter { city ->
                                    city.city.contains(searchQuery, ignoreCase = true) ||
                                            city.iataCode.contains(searchQuery, ignoreCase = true)
                                }
                                LazyColumn(
                                    modifier = Modifier.heightIn(max = 300.dp)
                                ) {
                                    items(filteredCities) { city ->
                                        val airportName = cityViewModel.popularAirports.find { it.first == city.iataCode }?.second?.second ?: ""
                                        CityOption(
                                            code = city.iataCode,
                                            name = city.city,
                                            airportName = airportName,
                                            onSelect = { code, name, airport ->
                                                if (showCityPicker == "from") {
                                                    flightViewModel.updateFromCity(code, name, airport)
                                                    println("FlightSearchScreen: Selected departure city (filtered) - code=$code, name=$name, airport=$airport")
                                                } else {
                                                    flightViewModel.updateToCity(code, name, airport)
                                                    println("FlightSearchScreen: Selected destination city (filtered) - code=$code, name=$name, airport=$airport")
                                                }
                                                showCityPicker = null
                                                searchQuery = ""
                                            }
                                        )
                                    }
                                }
                                println("FlightSearchScreen: City picker - filtered cities count=${filteredCities.size}")
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = {
                    showCityPicker = null
                    println("FlightSearchScreen: City picker dialog cancelled")
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Passenger Selection Dialog
    if (showPassengerDialog) {
        var adultCount by remember { mutableStateOf(searchState.adultCount) }
        var childCount by remember { mutableStateOf(searchState.childCount) }
        var infantCount by remember { mutableStateOf(searchState.infantCount) }

        // Log when passenger dialog opens
        println("FlightSearchScreen: Passenger dialog opened - adults=$adultCount, children=$childCount, infants=$infantCount")

        AlertDialog(
            onDismissRequest = {
                showPassengerDialog = false
                println("FlightSearchScreen: Passenger dialog dismissed")
            },
            title = { Text("Select Passengers") },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    PassengerTypeSelector(
                        title = "Adults", // Fixed the title
                        subtitle = "Age 12+",
                        count = adultCount,
                        onIncrement = {
                            if (adultCount < 9) adultCount++
                            println("FlightSearchScreen: Adult count incremented - new count=$adultCount")
                        },
                        onDecrement = {
                            if (adultCount > 1) adultCount--
                            println("FlightSearchScreen: Adult count decremented - new count=$adultCount")
                        }
                    )
                    PassengerTypeSelector(
                        title = "Children",
                        subtitle = "Age 2-11",
                        count = childCount,
                        onIncrement = {
                            if (childCount < 8) childCount++
                            println("FlightSearchScreen: Child count incremented - new count=$childCount")
                        },
                        onDecrement = {
                            if (childCount > 0) childCount--
                            println("FlightSearchScreen: Child count decremented - new count=$childCount")
                        }
                    )
                    PassengerTypeSelector(
                        title = "Infants",
                        subtitle = "Under 2",
                        count = infantCount,
                        onIncrement = {
                            if (infantCount < adultCount) infantCount++
                            println("FlightSearchScreen: Infant count incremented - new count=$infantCount")
                        },
                        onDecrement = {
                            if (infantCount > 0) infantCount--
                            println("FlightSearchScreen: Infant count decremented - new count=$infantCount")
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        flightViewModel.updatePassengerCounts(adultCount, childCount, infantCount)
                        showPassengerDialog = false
                        println("FlightSearchScreen: Passenger counts updated - adults=$adultCount, children=$childCount, infants=$infantCount")
                    }
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPassengerDialog = false
                    println("FlightSearchScreen: Passenger dialog cancelled")
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Cabin Class Selection Dialog
    if (showCabinClassDialog) {
        // Log when cabin class dialog opens
        println("FlightSearchScreen: Cabin class dialog opened - current cabinClass=${searchState.cabinClass}")

        AlertDialog(
            onDismissRequest = {
                showCabinClassDialog = false
                println("FlightSearchScreen: Cabin class dialog dismissed")
            },
            title = { Text("Select Cabin Class") },
            text = {
                Column {
                    FlightViewModel.CabinClass.values().forEach { cabinClass ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    flightViewModel.updateCabinClass(cabinClass)
                                    showCabinClassDialog = false
                                    println("FlightSearchScreen: Cabin class selected - cabinClass=$cabinClass")
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                cabinClass.displayName(),
                                fontSize = 16.sp,
                                color = if (cabinClass == searchState.cabinClass) Color(23, 111, 243) else Color.Black
                            )
                            if (cabinClass == searchState.cabinClass) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(23, 111, 243)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = {
                    showCabinClassDialog = false
                    println("FlightSearchScreen: Cabin class dialog cancelled")
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val currentMoment = Clock.System.now()
        val today = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
        var selectedDateLocal by remember { mutableStateOf(today) }

        // Log when date picker dialog opens
        println("FlightSearchScreen: Date picker dialog opened - current selected date=$selectedDateLocal")

        AlertDialog(
            onDismissRequest = {
                showDatePicker = false
                println("FlightSearchScreen: Date picker dialog dismissed")
            },
            title = {
                Text(
                    "Select Date",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
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
                                println("FlightSearchScreen: Previous month clicked - new date=$selectedDateLocal")
                            }
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Previous month", tint = Color(23, 111, 243))
                        }

                        Text(
                            "${selectedDateLocal.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDateLocal.year}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        IconButton(
                            onClick = {
                                selectedDateLocal = selectedDateLocal.plus(1, DateTimeUnit.MONTH)
                                println("FlightSearchScreen: Next month clicked - new date=$selectedDateLocal")
                            }
                        ) {
                            Icon(Icons.Default.ChevronRight, "Next month", tint = Color(23, 111, 243))
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
                                color = Color(0xFF424242)
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
                                                    color = if (isSelected) Color(23, 111, 243) else Color.Transparent,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .clickable(
                                                    enabled = currentDate >= today
                                                ) {
                                                    selectedDateLocal = currentDate
                                                    println("FlightSearchScreen: Date selected in picker - date=$selectedDateLocal")
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = dayCounter.toString(),
                                                color = when {
                                                    isSelected -> Color.White
                                                    isToday -> Color(23, 111, 243)
                                                    currentDate < today -> Color(0xFF424242).copy(alpha = 0.5f)
                                                    else -> Color.Black
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
                        flightViewModel.updateDate(formatDate(selectedDateLocal))
                        showDatePicker = false
                        println("FlightSearchScreen: Date picker confirmed - selected date=${formatDate(selectedDateLocal)}")
                    }
                ) {
                    Text("OK", color = Color(23, 111, 243))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    println("FlightSearchScreen: Date picker cancelled")
                }) {
                    Text("Cancel", color = Color(23, 111, 243))
                }
            }
        )
    }
}

@Composable
private fun CityOption(
    code: String,
    name: String,
    airportName: String = "",
    onSelect: (String, String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(code, name, airportName) }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    code,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF424242)
                )
            }
            if (airportName.isNotEmpty()) {
                Text(
                    airportName,
                    color = Color(0xFF424242),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.Flight,
            contentDescription = null,
            tint = Color(23, 111, 243),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun TabItem(
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

private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

private fun formatDate(date: LocalDate): String {
    val dayOfWeek = date.dayOfWeek.name.take(3)
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.month.name.take(3)
    val year = date.year.toString().takeLast(2)
    return "$dayOfWeek, $day $month $year"
}

private fun formatDateForApi(date: String): String {
    val parts = date.split(", ", " ")
    if (parts.size < 4) return "2025-06-08" // Fallback to current date
    val day = parts[1].padStart(2, '0')
    val month = when (parts[2]) {
        "Jan" -> "01"
        "Feb" -> "02"
        "Mar" -> "03"
        "Apr" -> "04"
        "May" -> "05"
        "Jun" -> "06"
        "Jul" -> "07"
        "Aug" -> "08"
        "Sep" -> "09"
        "Oct" -> "10"
        "Nov" -> "11"
        "Dec" -> "12"
        else -> "06"
    }
    val year = "20${parts[3]}"
    return "$year-$month-$day"
}

@Composable
private fun PassengerTypeSelector(
    title: String,
    subtitle: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 14.sp, color = Color(0xFF424242))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(23, 111, 243).copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = Color(23, 111, 243)
                )
            }
            Text(
                count.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.widthIn(min = 24.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(23, 111, 243).copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = Color(23, 111, 243)
                )
            }
        }
    }
}

//@Composable
//private fun FlightItem(
//    flight: FlightDTO,
//    cabinClass: String,
//    totalPassengers: Int,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .background(Color.White)
//                .padding(12.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = flight.airlineCode,
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//                Text(
//                    text = "${flight.price.toInt()} ${flight.currency}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = flight.departure.iataCode,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black
//                    )
//                    Text(
//                        text = flight.departure.time.toLocalDateTime(TimeZone.of("Asia/Kolkata"))
//                            .toString().substring(11, 16),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color(0xFF424242)
//                    )
//                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = formatDuration(flight.duration),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color(0xFF424242)
//                    )
//                    Text(
//                        text = "Direct",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color(0xFF424242)
//                    )
//                }
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(
//                        text = flight.arrival.iataCode,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black
//                    )
//                    Text(
//                        text = flight.arrival.time.toLocalDateTime(TimeZone.of("Asia/Kolkata"))
//                            .toString().substring(11, 16),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color(0xFF424242)
//                    )
//                }
//            }
//        }
//    }
//}
//
//private fun formatDuration(duration: String): String {
//    val hours = duration.substringAfter("PT").substringBefore("H").toIntOrNull() ?: 0
//    val minutes = duration.substringAfter("H").substringBefore("M").toIntOrNull() ?: 0
//    return "${hours}h ${minutes}m"
//}