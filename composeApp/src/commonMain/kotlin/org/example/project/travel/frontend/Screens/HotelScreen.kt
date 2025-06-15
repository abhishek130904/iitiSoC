package org.example.project.travel.frontend.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.network.HotelApiClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.example.project.travel.frontend.viewModel.HotelViewModel

interface HotelScreenComponent : ComponentContext {
    val flightPrice: Double
    val flightCurrency: String
    fun navigateTo(screen: Screen)
    fun goBack()
}

class HotelScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    override val flightPrice: Double,
    override val flightCurrency: String
) : HotelScreenComponent, ComponentContext by componentContext {
    override fun navigateTo(screen: Screen) {
        rootComponent.navigateTo(screen)
    }

    override fun goBack() {
        rootComponent.pop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelScreen(
    flightPrice: Double,
    flightCurrency: String,
    onNavigateBack: () -> Unit
) {
    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    val apiClient = remember { HotelApiClient(httpClient) }
    val viewModel = remember { HotelViewModel(apiClient) }

    val cities = viewModel.getCities()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val hotels by viewModel.hotels.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Hotel,
                            contentDescription = null,
                            tint = Color(0xFF286DC8),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(28.dp)
                        )
                        Text(
                            text = "Hotel Finder",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF286DC8)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF286DC8)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF286DC8)
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF286DC8)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(48.dp)
                ) {
                    Text("Back", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = { /* Placeholder: Functionality to be added later */ },
                    enabled = false, // Disabled for now
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF286DC8),
                        disabledContainerColor = Color(0xFFB0BEC5)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(48.dp)
                ) {
                    Text("Next", color = Color.White, fontSize = 16.sp)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // City Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = {},
                    label = { Text("Select City") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF286DC8),
                        unfocusedBorderColor = Color(0xFF90A4AE),
                        focusedLabelColor = Color(0xFF286DC8),
                        cursorColor = Color(0xFF286DC8),
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    readOnly = true,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    city,
                                    color = if (city == selectedCity) Color(0xFF286DC8) else Color.Black,
                                    fontWeight = if (city == selectedCity) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                viewModel.onCitySelected(city)
                                expanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            // Loading State
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(28.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(42.dp),
                        color = Color(0xFF286DC8),
                        strokeWidth = 4.dp
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Loading",
                            tint = Color(0xFF78909C),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 6.dp)
                        )
                        Text(
                            text = "Loading hotels...",
                            color = Color(0xFF78909C),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            // Error State
            error?.let { errorMessage ->
                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = " $errorMessage",
                            fontSize = 16.sp,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            // Hotel List
            if (!isLoading && error == null) {
                if (hotels.isEmpty()) {
                    Spacer(Modifier.height(38.dp))
                    Text(
                        text = "No hotels found for $selectedCity.",
                        color = Color(0xFF78909C),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                text = "Hotels in $selectedCity",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF286DC8),
                                modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                            )
                        }
                        items(hotels) { hotel ->
                            HotelCard(hotel = hotel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotelCard(hotel: AccommodationDTO) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Placeholder image box
            Box(
                modifier = Modifier
                    .size(110.dp, 80.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Hotel,
                    contentDescription = "Hotel",
                    tint = Color(0xFFB0BEC5),
                    modifier = Modifier.size(50.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 2.dp, bottom = 2.dp, end = 2.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = hotel.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF286DC8)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${hotel.rating}/5 ",
                        color = Color(0xFFF9A825),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = null,
                        tint = Color(0xFFF9A825),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = "₹${hotel.pricePerNight.toInt()} ${hotel.currency} per night",
                    color = Color(0xFF455A64),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                if (hotel.amenities.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 1.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        hotel.amenities.take(3).forEach { amenity ->
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE8F5E9),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    text = amenity,
                                    fontSize = 12.sp,
                                    color = Color(0xFF388E3C),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        if (hotel.amenities.size > 3) {
                            Text(
                                text = "+${hotel.amenities.size - 3} more",
                                fontSize = 12.sp,
                                color = Color(0xFF78909C),
                                modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "Location: (${hotel.latitude}, ${hotel.longitude})",
                    color = Color(0xFF78909C),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
                if (hotel.airbnbUrl.isNotEmpty()) {
                    Button(
                        onClick = { uriHandler.openUri(hotel.airbnbUrl) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(1.dp),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(32.dp)
                    ) {
                        Text(
                            text = "View on Airbnb",
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    // Validate coordinates (latitude: [-90, 90], longitude: [-180, 180])
                    val isValidLocation = hotel.latitude in -90.0..90.0 && hotel.longitude in -180.0..180.0

                    if (isValidLocation) {
                        val googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=${hotel.latitude},${hotel.longitude}"
                        Button(
                            onClick = { uriHandler.openUri(googleMapsUrl) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)), // Light blue to distinguish from Airbnb button
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(1.dp),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .height(32.dp)
                        ) {
                            Text(
                                text = "Show on Google Maps",
                                color = Color(0xFF1976D2), // Darker blue for text
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}