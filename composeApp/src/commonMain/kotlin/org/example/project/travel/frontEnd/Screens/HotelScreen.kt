package org.example.project.travel.frontend.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.travel.model.dto.FlightDTO
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
import kotlinx.coroutines.delay

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
    override val city: String = selectedFlight.arrival.iataCode

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
    val primaryBlue = Color(0xFF176FF3)
    val lightBlue = Color(0xFFE3F2FD)

    val citySearchViewModel = remember { CitySearchViewModel<DestinationCity>() }
    var selectedCity by remember { mutableStateOf<DestinationCity?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedHotel by remember { mutableStateOf<AccommodationDTO?>(null) }
    var showContent by remember { mutableStateOf(false) }

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
    val hotelViewModel = remember(selectedCity?.city) {
        HotelViewModel(apiClient, selectedCity?.city ?: "")
    }
    val hotels by hotelViewModel.hotels.collectAsState()
    val isHotelsLoading by hotelViewModel.isLoading.collectAsState()
    val hotelError by hotelViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty() && selectedCity == null) {
            citySearchViewModel.searchCities(searchQuery)
        }
    }

    val banner: Painter = painterResource(Res.drawable.background_image)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryBlue.copy(alpha = 0.1f),
                        Color.White,
                        lightBlue.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        // Floating hotel-themed elements
        FloatingHotelElements()

        Scaffold(
            topBar = {
                EnhancedTopBar(
                    primaryBlue = primaryBlue,
                    onNavigateBack = onNavigateBack
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = selectedHotel != null,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    )
                ) {
                    EnhancedBottomBar(
                        selectedHotel = selectedHotel,
                        primaryBlue = primaryBlue,
                        onNavigateToNext = onNavigateToNext
                    )
                }
            },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Enhanced Banner
                AnimatedVisibility(
                    visible = showContent,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(800))
                ) {
                    EnhancedBanner(
                        banner = banner,
                        primaryBlue = primaryBlue
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Search Section
                AnimatedVisibility(
                    visible = showContent,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(600, 200, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(600, 200))
                ) {
                    SearchSection(
                        selectedCity = selectedCity,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onCityCleared = {
                            selectedCity = null
                            searchQuery = ""
                        },
                        primaryBlue = primaryBlue,
                        lightBlue = lightBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // City Results
                if (selectedCity == null && searchQuery.isNotEmpty()) {
                    CitySearchResults(
                        cities = cities,
                        isLoading = isLoading,
                        error = error,
                        primaryBlue = primaryBlue,
                        onCitySelected = { city ->
                            selectedCity = city
                            onCitySelected(city)
                        }
                    )
                }

                // Hotels Section
                if (selectedCity != null) {
                    HotelsSection(
                        selectedCity = selectedCity!!,
                        hotels = hotels,
                        isLoading = isHotelsLoading,
                        error = hotelError,
                        selectedHotel = selectedHotel,
                        onHotelSelected = { selectedHotel = it },
                        primaryBlue = primaryBlue,
                        lightBlue = lightBlue,
                        showContent = showContent
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingHotelElements() {
    val hotelIcons = listOf("🏨", "🛏️", "🗝️", "🧳", "⭐", "🏙️")

    hotelIcons.forEachIndexed { index, icon ->
        var animatedY by remember { mutableStateOf((0..100).random().toFloat()) }
        var animatedX by remember { mutableStateOf((0..100).random().toFloat()) }

        LaunchedEffect(Unit) {
            while (true) {
                animate(
                    initialValue = animatedY,
                    targetValue = if (animatedY > 50) 0f else 100f,
                    animationSpec = tween(
                        durationMillis = (5000..9000).random(),
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    animatedY = value
                }
                animate(
                    initialValue = animatedX,
                    targetValue = if (animatedX > 50) 0f else 100f,
                    animationSpec = tween(
                        durationMillis = (4000..7000).random(),
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    animatedX = value
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(
                    x = (animatedX * 3.5).dp,
                    y = (animatedY * 7).dp
                )
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.alpha(0.2f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedTopBar(
    primaryBlue: Color,
    onNavigateBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Hotel,
                        contentDescription = "Hotel",
                        tint = primaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Hotel Finder",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            primaryBlue.copy(alpha = 0.1f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = primaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun EnhancedBanner(
    banner: Painter,
    primaryBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(200.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp)
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
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏨",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Find Your Perfect Stay",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Discover amazing hotels worldwide",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchSection(
    selectedCity: DestinationCity?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCityCleared: () -> Unit,
    primaryBlue: Color,
    lightBlue: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        if (selectedCity == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = {
                        Text(
                            "Search for a city",
                            color = primaryBlue.copy(alpha = 0.7f)
                        )
                    },
                    placeholder = {
                        Text(
                            "Enter destination...",
                            color = Color.Gray
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = primaryBlue
                                )
                            }
                        }
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = primaryBlue
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.Transparent,
                        focusedLabelColor = primaryBlue,
                        cursorColor = primaryBlue
                    ),
                    singleLine = true
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightBlue
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(primaryBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.LocationCity,
                                contentDescription = "City",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = selectedCity.city,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = primaryBlue
                            )
                            Text(
                                text = "${selectedCity.state}, ${selectedCity.country}",
                                color = primaryBlue.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onCityCleared,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CitySearchResults(
    cities: List<DestinationCity>,
    isLoading: Boolean,
    error: String?,
    primaryBlue: Color,
    onCitySelected: (DestinationCity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        when {
            isLoading -> {
                EnhancedLoadingCard(
                    message = "🔎 Searching cities...",
                    primaryBlue = primaryBlue
                )
            }
            error != null -> {
                EnhancedErrorCard(
                    error = error,
                    primaryBlue = primaryBlue
                )
            }
            cities.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(cities) { index, city ->
                        val animationDelay = index * 100
                        EnhancedCityItem(
                            city = city,
                            primaryBlue = primaryBlue,
                            animationDelay = animationDelay,
                            onCitySelected = onCitySelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HotelsSection(
    selectedCity: DestinationCity,
    hotels: List<AccommodationDTO>,
    isLoading: Boolean,
    error: String?,
    selectedHotel: AccommodationDTO?,
    onHotelSelected: (AccommodationDTO) -> Unit,
    primaryBlue: Color,
    lightBlue: Color,
    showContent: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Section Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Hotel,
                    contentDescription = "Hotels",
                    tint = primaryBlue,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Hotels in ${selectedCity.city}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                EnhancedLoadingCard(
                    message = "🏨 Loading hotels...",
                    primaryBlue = primaryBlue
                )
            }
            error != null -> {
                EnhancedErrorCard(
                    error = error,
                    primaryBlue = primaryBlue
                )
            }
            hotels.isEmpty() -> {
                EnhancedEmptyCard(
                    message = "😔 No hotels found for ${selectedCity.city}",
                    primaryBlue = primaryBlue
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(hotels) { index, hotel ->
                        val animationDelay = if (showContent) index * 150 else 0
                        EnhancedHotelCard(
                            hotel = hotel,
                            isSelected = selectedHotel == hotel,
                            primaryBlue = primaryBlue,
                            lightBlue = lightBlue,
                            animationDelay = animationDelay,
                            onClick = { onHotelSelected(hotel) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedCityItem(
    city: DestinationCity,
    primaryBlue: Color,
    animationDelay: Int,
    onCitySelected: (DestinationCity) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(400))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clickable { onCitySelected(city) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    primaryBlue,
                                    primaryBlue.copy(alpha = 0.8f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocationCity,
                        contentDescription = "City",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

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

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Select",
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedHotelCard(
    hotel: AccommodationDTO,
    isSelected: Boolean,
    primaryBlue: Color,
    lightBlue: Color,
    animationDelay: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) 16.dp else 8.dp,
        animationSpec = tween(300)
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(300)
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(600))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(animatedElevation, RoundedCornerShape(20.dp))
                .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) primaryBlue else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    lightBlue.copy(alpha = 0.3f)
                else
                    Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Hotel Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hotel.name,
                            fontSize = 20.sp,
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Section
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = primaryBlue.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = "Price",
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${hotel.currency} ${hotel.pricePerNight.toInt()} per night",
                            color = primaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Amenities
                if (hotel.amenities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Amenities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryBlue,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hotel.amenities.take(4)) { amenity ->
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
                                shape = RoundedCornerShape(20.dp),
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = lightBlue.copy(alpha = 0.5f)
                                )
                            )
                        }
                        if (hotel.amenities.size > 4) {
                            item {
                                Text(
                                    text = "+${hotel.amenities.size - 4} more",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Spacer(modifier = Modifier.height(16.dp))

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
                            shape = RoundedCornerShape(12.dp),
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
                                fontWeight = FontWeight.SemiBold
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
                            shape = RoundedCornerShape(12.dp),
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
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Selection Indicator
                if (isSelected) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Selected",
                            color = primaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedBottomBar(
    selectedHotel: AccommodationDTO?,
    primaryBlue: Color,
    onNavigateToNext: (AccommodationDTO) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            if (selectedHotel != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        Icons.Default.Hotel,
                        contentDescription = "Selected Hotel",
                        tint = primaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedHotel.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = primaryBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${selectedHotel.currency} ${selectedHotel.pricePerNight.toInt()}/night",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Button(
                onClick = { selectedHotel?.let { onNavigateToNext(it) } },
                enabled = selectedHotel != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBlue,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Continue to Activities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedLoadingCard(
    message: String,
    primaryBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = primaryBlue,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = primaryBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EnhancedErrorCard(
    error: String,
    primaryBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EnhancedEmptyCard(
    message: String,
    primaryBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏨",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = primaryBlue,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
