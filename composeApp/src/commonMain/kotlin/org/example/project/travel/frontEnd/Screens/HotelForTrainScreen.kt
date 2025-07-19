package org.example.project.travel.frontend.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel.model.dto.AccommodationDTO
import com.example.travel.network.HotelApiClient
import com.example.travel.network.TrainSearchResultDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontend.viewModel.HotelViewModel
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.background_image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelForTrainScreen(
    selectedTrain: TrainSearchResultDTO,
    selectedCoach: String,
    fare: Int,
    onNavigateBack: () -> Unit,
    onNavigateToNext: (AccommodationDTO) -> Unit,
    onCitySelected: (DestinationCity) -> Unit
) {
    val primaryBlue = Color(0xFF176FF3)

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
    val hotelViewModel = remember(selectedCity?.city ?: selectedTrain.to_station_name) {
        HotelViewModel(apiClient, selectedCity?.city ?: selectedTrain.to_station_name)
    }
    val hotels by hotelViewModel.hotels.collectAsState()
    val isHotelsLoading by hotelViewModel.isLoading.collectAsState()
    val hotelError by hotelViewModel.error.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty() && selectedCity == null) {
            citySearchViewModel.searchCities(searchQuery)
        }
    }

    val banner: Painter = painterResource(Res.drawable.background_image)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Hotel,
                            contentDescription = "Hotel",
                            tint = primaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Hotel Finder",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            if (selectedHotel != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = { selectedHotel?.let { onNavigateToNext(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Continue",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Continue",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box {
                        Image(
                            painter = banner,
                            contentDescription = "Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Find Your Perfect Stay",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Discover amazing hotels in India",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Search Section
            item {
                if (selectedCity == null) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search for a city") },
                        placeholder = { Text("Enter destination...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            focusedLabelColor = primaryBlue,
                            cursorColor = primaryBlue
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(primaryBlue.copy(alpha = 0.08f))
                            .clickable { selectedCity = null },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = primaryBlue, modifier = Modifier.padding(12.dp))
                        Text(
                            selectedCity!!.city,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = primaryBlue, modifier = Modifier.padding(12.dp))
                    }
                }
            }

            // City Search Results
            if (selectedCity == null && searchQuery.isNotEmpty()) {
                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = primaryBlue)
                        }
                    }
                } else if (error != null) {
                    item {
                        Text(error!!, color = Color.Red, modifier = Modifier.padding(8.dp))
                    }
                } else {
                    items(cities) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCity = city
                                    onCitySelected(city)
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = primaryBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(city.city, color = primaryBlue, fontSize = 16.sp)
                        }
                    }
                }
            }

            // Hotel Results
            if (selectedCity != null) {
                if (isHotelsLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = primaryBlue)
                        }
                    }
                } else if (hotelError != null) {
                    item {
                        Text(hotelError!!, color = Color.Red, modifier = Modifier.padding(8.dp))
                    }
                } else {
                    items(hotels) { hotel ->
                        ExpandableHotelCard(
                            hotel = hotel,
                            isSelected = selectedHotel == hotel,
                            primaryBlue = primaryBlue,
                            onClick = { selectedHotel = hotel }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableHotelCard(
    hotel: AccommodationDTO,
    isSelected: Boolean,
    primaryBlue: Color,
    onClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var isFavorite by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFFE3F2FD)
            else
                Color.White
        ),
        border = if (isSelected)
            BorderStroke(2.dp, primaryBlue)
        else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Main Hotel Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hotel.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (index < hotel.rating.toInt())
                                    Color(0xFFFFC107)
                                else
                                    Color.Gray.copy(alpha = 0.3f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${hotel.rating}/5",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isFavorite = !isFavorite }
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = { isExpanded = !isExpanded }
                    ) {
                        Icon(
                            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = primaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AttachMoney,
                    contentDescription = "Price",
                    tint = primaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${hotel.currency} ${hotel.pricePerNight.toInt()}/night",
                    color = primaryBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            // Expandable Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Amenities Section
                    if (hotel.amenities.isNotEmpty()) {
                        Text(
                            text = "Amenities",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryBlue,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            items(hotel.amenities.take(6)) { amenity ->
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            amenity,
                                            fontSize = 12.sp,
                                            color = primaryBlue
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = primaryBlue
                                        )
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFFE3F2FD)
                                    )
                                )
                            }
                        }

                        if (hotel.amenities.size > 6) {
                            Text(
                                text = "+${hotel.amenities.size - 6} more amenities",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (hotel.airbnbUrl.isNotEmpty()) {
                            Button(
                                onClick = { uriHandler.openUri(hotel.airbnbUrl) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF5A5F)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.OpenInNew,
                                    contentDescription = "Airbnb",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Airbnb",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        val isValidLocation = hotel.latitude in -90.0..90.0 &&
                                hotel.longitude in -180.0..180.0
                        if (isValidLocation) {
                            val googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=${hotel.latitude},${hotel.longitude}"
                            Button(
                                onClick = { uriHandler.openUri(googleMapsUrl) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryBlue
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Maps",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Maps",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Selection Indicator
            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = primaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Selected",
                        color = primaryBlue,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
