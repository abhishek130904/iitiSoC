package org.example.project.travel.frontend.Screens.Transportation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.example.travel.model.dto.FlightDTO
import com.example.travel.viewmodel.FlightViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.ic_ai
import travelfrontend.composeapp.generated.resources.login_background

interface FlightDetailScreenComponent {
    val flights: StateFlow<List<FlightDTO>>
    val flightViewModel: FlightViewModel
    fun navigateTo(screen: Screen)
}

class FlightDetailScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    initialFlights: List<FlightDTO>
) : FlightDetailScreenComponent, ComponentContext by componentContext {
    override val flightViewModel = rootComponent.flightViewModel
    private val _flights = MutableStateFlow(initialFlights)
    override val flights: StateFlow<List<FlightDTO>> = _flights.asStateFlow()

    init {
        println("FlightDetailScreenComponentImpl: Constructor - initialFlights size=${initialFlights.size}")
        println("FlightDetailScreenComponentImpl: Initialized - flights=$_flights")
    }

    override fun navigateTo(screen: Screen) {
        println("FlightDetailScreenComponentImpl: Navigating to $screen")
        rootComponent.navigateTo(screen)
    }
}


@Composable
fun FlightDetailScreen(
    component: FlightDetailScreenComponent
) {
    val flights by component.flights.collectAsStateWithLifecycle()
    println("FlightDetailScreen: Composable started - flights=$flights")
    val searchState by component.flightViewModel.searchState.collectAsState()
    val filteredFlights = flights.filter { it.arrival.iataCode == component.flightViewModel.searchState.value.toCity }
    println("FlightDetailScreen: After filtering - filteredFlights=$filteredFlights")

    var selectedFlight by remember { mutableStateOf<FlightDTO?>(null) }

    println("FlightDetailScreen: Composable started - flights=$flights")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                println("FlightDetailScreen: Back button clicked")
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Flight Results",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (flights.isEmpty()) {
            println("FlightDetailScreen: Flights list is empty, showing 'No flights available'")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No flights available", color = Color.Gray)
            }
        } else {
            println("FlightDetailScreen: Flights list is not empty - flights count=${flights.size}")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(flights) { flight ->
                    println("FlightDetailScreen: Rendering flight item - airlineCode=${flight.airlineCode}, price=${flight.price}")
                    FlightItem(

                        flight = flight,
                        cabinClass = component.flightViewModel.searchState.value.cabinClass.displayName(),
                        totalPassengers = component.flightViewModel.searchState.value.let {
                            it.adultCount + it.childCount + it.infantCount
                        },
                        selectedFlight = selectedFlight,

                        onClick = {
                            selectedFlight = flight
                            println("FlightDetailScreen: Flight item clicked - airlineCode=${flight.airlineCode}")
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }
        if (selectedFlight != null) {
            Button(
                onClick = {
                    println("FlightDetailScreen: Next button clicked for flight - airlineCode=${selectedFlight?.airlineCode}")
                    component.navigateTo(
                        Screen.Hotel(
                            flightPrice = selectedFlight!!.price,
                            flightCurrency = selectedFlight!!.currency
                        )
                    )
                          // Add navigation or action for the "Next" button here
                },
                modifier = Modifier
//                    .align(Alignment.BottomCenter)
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
                    "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    DisposableEffect(Unit) {
        println("FlightDetailScreen: DisposableEffect - onStart")
        onDispose {
            println("FlightDetailScreen: DisposableEffect - onDispose, clearing flights")
            component.flightViewModel.clearFlights(component.flightViewModel)
        }
    }
}

@Composable
private fun FlightItem(
    flight: FlightDTO,
    cabinClass: String,
    totalPassengers: Int,
    selectedFlight: FlightDTO?,
    onClick: () -> Unit
) {
    // Color palette
    val directColor = Color(0xFF00796B)
    val bgGradStart = Color(0xFFF6FAFD)
    val bgGradEnd = Color(0xFFD8EAF5)
    val accent = Color(0xFF286DC8)
    val priceBg = Color(0xFFECF5FE)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick)
            .shadow(9.dp, RoundedCornerShape(18.dp))
            .then(
                if (flight == selectedFlight) {
                    Modifier.border(
                        border = BorderStroke(2.dp, Color(0xFF176FF3)), // Use a color that matches your theme (e.g., directColor)
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = bgGradStart)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(bgGradStart, bgGradEnd)
                    )
                )
                .padding(16.dp)
        ) {
            // Airline + Price Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .shadow(3.dp, CircleShape, ambientColor = Color.LightGray, spotColor = Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_ai),
                        contentDescription = "${flight.airlineCode} logo",
                        modifier = Modifier.size(34.dp).clip(CircleShape)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = flight.airlineCode,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
//                    Text(
//                        text = flight.operator,
//                        color = Color.Gray,
//                        fontSize = 13.sp
//                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    shadowElevation = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = priceBg
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        text = "${flight.price.toInt()} ${flight.currency}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = accent
                    )
                }
            }

            // Departure / Arrival Route Row
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Departure
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = flight.departure.iataCode,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    Text(
                        text = flight.departure.time.toLocalDateTime(TimeZone.of("Asia/Kolkata"))
                            .toString().substring(11, 16),
                        color = Color.DarkGray,
                        fontSize = 13.sp
                    )
                }

                // Flight path (dot → line → dot)
                Row(
                    Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(accent)
                    )
                    Spacer(Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(38.dp)
                            .background(Color.LightGray)
                    )
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Flight Direction",
                        tint = accent,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                rotationZ = 180f
                            }
                    )
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(38.dp)
                            .background(Color.LightGray)
                    )
                    Spacer(Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(accent)
                    )
                }

                // Arrival
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flight.arrival.iataCode,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    Text(
                        text = flight.arrival.time.toLocalDateTime(TimeZone.of("Asia/Kolkata"))
                            .toString().substring(11, 16),
                        color = Color.DarkGray,
                        fontSize = 13.sp
                    )
                }
            }

            // Meta Info: Duration & Direct & Cabin
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color(0xFFD6F5E8),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        text = formatDuration(flight.duration),
                        color = directColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
                if (true) { // You may use real direct/connecting logic based on DTO.
                    Surface(
                        color = Color(0xFFEAF8FF),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            text = "Direct",
                            color = accent,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
                Surface(
                    color = Color(0xFFFFF3E6),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        text = "Cabin: $cabinClass",
                        color = Color(0xFFE68800),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
                Surface(
                    color = Color(0xFFF6D3ED),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        text = "\uD83D\uDC65 $totalPassengers",
                        color = Color(0xFFC6267A),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


private fun formatDuration(duration: String): String {
    val hours = duration.substringAfter("PT").substringBefore("H").toIntOrNull() ?: 0
    val minutes = duration.substringAfter("H").substringBefore("M").toIntOrNull() ?: 0
    return "${hours}h ${minutes}m"
}