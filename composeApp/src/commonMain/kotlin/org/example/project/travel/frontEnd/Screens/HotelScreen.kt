package org.example.project.travel.frontend.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.network.HotelApiClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontend.viewModel.HotelViewModel
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.background_image
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material.icons.filled.Clear
import com.example.travel.model.dto.FlightDTO

interface HotelScreenComponent : ComponentContext {
    val city: String
    val selectedFlight: FlightDTO
    fun navigateTo(screen: Screen)
    fun goBack()
}

class HotelScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    override val selectedFlight: FlightDTO
) : HotelScreenComponent, ComponentContext by componentContext {
    override val city: String = selectedFlight.arrival.iataCode // Or a mapped city name if available
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
    selectedFlight: FlightDTO,
    onNavigateBack: () -> Unit,
    onNavigateToNext: (AccommodationDTO) -> Unit,
    onCitySelected: (DestinationCity) -> Unit
) {
    val citySearchViewModel = remember { CitySearchViewModel<DestinationCity>() }
    var selectedCity by remember { mutableStateOf<DestinationCity?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedHotel by remember { mutableStateOf<AccommodationDTO?>(null) }
    val cities by citySearchViewModel.cities.collectAsState()
    val isLoading by citySearchViewModel.isLoading.collectAsState()
    val error by citySearchViewModel.error.collectAsState()

    // Hotel API and ViewModel
    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) { json() }
        }
    }
    val apiClient = remember { HotelApiClient(httpClient) }
    val hotelViewModel = remember(selectedCity?.city) { HotelViewModel(apiClient, selectedCity?.city ?: "") }
    val hotels by hotelViewModel.hotels.collectAsState()
    val isHotelsLoading by hotelViewModel.isLoading.collectAsState()
    val hotelError by hotelViewModel.error.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty() && selectedCity == null) citySearchViewModel.searchCities(searchQuery)
    }

    val banner: Painter = painterResource(Res.drawable.background_image)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hotel Finder", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF176FF3))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF176FF3))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color(0xFF176FF3))
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { if (selectedHotel != null) onNavigateToNext(selectedHotel!!) },
                    enabled = selectedHotel != null,
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedHotel != null) Color(0xFF176FF3) else Color(0xFFB0BEC5)),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("Next", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF5F5F5), Color(0xFFE3F2FD))))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                Image(
                    painter = banner,
                    contentDescription = "Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f))
                )
                Text(
                    text = "Find Your Perfect Stay",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            // City Search Bar or Chip
            if (selectedCity == null) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search for a city") },
                    placeholder = { Text("Enter destination...") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .shadow(10.dp, RoundedCornerShape(32.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF176FF3),
                        unfocusedBorderColor = Color(0xFFB0BEC5),
                        containerColor = Color.White
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (isLoading) {
                    Text("🔎 Searching...", fontSize = 18.sp, color = Color(0xFF176FF3), modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (error != null) {
                    Text(error ?: "An unknown error occurred.", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (cities.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(cities) { city ->
                            CityItem(city = city, isSelected = false, onCitySelected = {
                                selectedCity = it
                                onCitySelected(it)
                            })
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = { selectedCity = null; searchQuery = "" },
                        label = { Text(selectedCity!!.city, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.LocationCity, contentDescription = null, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        },
                        shape = RoundedCornerShape(32.dp),
                        colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (selectedCity != null) "Hotels in ${selectedCity!!.city}" else "Select a city to view hotels",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF176FF3),
                modifier = Modifier.padding(vertical = 12.dp)
            )
            if (selectedCity != null) {
                if (isHotelsLoading) {
                    Text("🏨 Loading hotels...", fontSize = 18.sp, color = Color(0xFF176FF3), modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (hotelError != null) {
                    Text(hotelError ?: "An unknown error occurred.", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (hotels.isEmpty()) {
                    Text("😔 No hotels found for ${selectedCity!!.city}.", color = Color(0xFF78909C), fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp), textAlign = TextAlign.Center)
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(hotels) { hotel ->
                            HotelCardModern(
                                hotel = hotel,
                                isSelected = selectedHotel == hotel,
                                onClick = { selectedHotel = hotel }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CityItem(city: DestinationCity, isSelected: Boolean, onCitySelected: (DestinationCity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF4A90E2) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCitySelected(city) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationCity,
                contentDescription = "City",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF4A90E2), CircleShape)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city.city,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "${city.state}, ${city.country}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun HotelCardModern(hotel: AccommodationDTO, isSelected: Boolean, onClick: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color.White)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color(0xFF176FF3) else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = hotel.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF176FF3)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFF44336) else Color(0xFFB0BEC5),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${hotel.rating}/5",
                        color = Color(0xFFF9A825),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = hotel.currency +" "+ hotel.pricePerNight.toInt() + " per night",
                        color = Color(0xFF176FF3),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                if (hotel.amenities.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 1.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        hotel.amenities.take(3).forEach { amenity ->
                            AssistChip(
                                onClick = {},
                                label = { Text(amenity, fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.Hotel, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                shape = RoundedCornerShape(50),
                                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFE3F2FD))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (hotel.airbnbUrl.isNotEmpty()) {
                        Button(
                            onClick = { uriHandler.openUri(hotel.airbnbUrl) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(1.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("View on Airbnb", color = Color(0xFF388E3C), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    val isValidLocation = hotel.latitude in -90.0..90.0 && hotel.longitude in -180.0..180.0
                    if (isValidLocation) {
                        val googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=${hotel.latitude},${hotel.longitude}"
                        Button(
                            onClick = { uriHandler.openUri(googleMapsUrl) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                            shape = RoundedCornerShape(8.dp),
                            elevation = ButtonDefaults.buttonElevation(1.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Show on Google Maps", color = Color(0xFF1976D2), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}