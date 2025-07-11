package org.example.project.travel.frontend.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.background_image

interface CitySearchScreenComponent {
    fun onCitySelected(city: DestinationCity)
    fun onBack()
    fun onStateSelected(stateName: String)
}

class CitySearchScreenComponentImpl(
    private val componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : CitySearchScreenComponent, ComponentContext by componentContext {
    override fun onCitySelected(city: DestinationCity) {
        rootComponent.navigateTo(Screen.CityDetails(city.id.toString(), city.city))
    }
    override fun onBack() {
        rootComponent.pop()
    }
    override fun onStateSelected(stateName: String) {
        rootComponent.navigateTo(Screen.StateScreen(stateName))
    }
}

data class FamousPlace(
    val destination: DestinationCity,
    val imageRes: String,
    val description: String,
    val rating: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    component: CitySearchScreenComponent,
    viewModel: CitySearchViewModel<DestinationCity>
) {
    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var citySearchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<DestinationCity?>(null) }
    var stateSearchQuery by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf<String?>(null) }
    var showStateDropdown by remember { mutableStateOf(false) }

    val allStates = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana",
        "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
        "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    )

    val filteredStates = if (stateSearchQuery.isEmpty()) allStates
    else allStates.filter { it.contains(stateSearchQuery, ignoreCase = true) }
    val filteredCities = if (citySearchQuery.isEmpty()) cities
    else cities.filter { it.city.contains(citySearchQuery, ignoreCase = true) }

    LaunchedEffect(cities) {
        if (cities.isNotEmpty() && selectedCity == null) {
            selectedCity = cities.first()
        }
    }

    val famousPlaces = listOf(
        FamousPlace(
            DestinationCity(id = 800, city = "Panjim-Goa", state = "Goa", country = "India", cityCode = 1260607),
            "drawable/beach.jpg", "Beautiful beaches & nightlife", 4.5f
        ),
        FamousPlace(
            DestinationCity(id = 1643, city = "Jaipur", state = "Rajasthan", country = "India", cityCode = 1269515),
            "drawable/fort.jpg", "Pink City with royal heritage", 4.7f
        ),
        FamousPlace(
            DestinationCity(id = 2598, city = "Agra", state = "Uttar Pradesh", country = "India", cityCode = 1279259),
            "drawable/tajmahal.png", "Home to the iconic Taj Mahal", 4.8f
        ),
        FamousPlace(
            DestinationCity(id = 76, city = "Varanasi", state = "Uttar Pradesh", country = "India", cityCode = 1253405),
            "drawable/temple.jpg", "Spiritual capital of India", 4.6f
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2),
                        Color(0xFFF093FB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header Section
            AnimatedHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Search Cards
            SearchSection(
                stateSearchQuery = stateSearchQuery,
                onStateSearchChange = {
                    stateSearchQuery = it
                    showStateDropdown = it.isNotEmpty()
                },
                citySearchQuery = citySearchQuery,
                onCitySearchChange = {
                    citySearchQuery = it
                    viewModel.searchCities(it)
                },
                showStateDropdown = showStateDropdown,
                filteredStates = filteredStates,
                onStateSelected = { state ->
                    selectedState = state
                    showStateDropdown = false
                    stateSearchQuery = state
                    component.onStateSelected(state)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content Section
            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading -> LoadingSection()
                    error != null -> ErrorSection(error = error ?: "Unknown error")
                    citySearchQuery.isEmpty() -> FamousPlacesSection(
                        places = famousPlaces,
                        onCitySelected = {
                            selectedCity = it
                            component.onCitySelected(it)
                        }
                    )
                    filteredCities.isEmpty() -> EmptyStateSection(query = citySearchQuery)
                    else -> CitiesListSection(
                        cities = filteredCities,
                        selectedCity = selectedCity,
                        onCitySelected = {
                            selectedCity = it
                            component.onCitySelected(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedHeader() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(800))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✈️ Discover India",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Find your perfect destination",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSection(
    stateSearchQuery: String,
    onStateSearchChange: (String) -> Unit,
    citySearchQuery: String,
    onCitySearchChange: (String) -> Unit,
    showStateDropdown: Boolean,
    filteredStates: List<String>,
    onStateSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // State Search
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                OutlinedTextField(
                    value = stateSearchQuery,
                    onValueChange = onStateSearchChange,
                    label = { Text("🏛️ Search States", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Search State",
                            tint = Color(0xFF667eea)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF667eea),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        cursorColor = Color(0xFF667eea)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // State Dropdown
                AnimatedVisibility(
                    visible = showStateDropdown && filteredStates.isNotEmpty()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(filteredStates) { state ->
                            StateItem(
                                state = state,
                                onStateSelected = onStateSelected
                            )
                        }
                    }
                }
            }
        }

        // City Search
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            OutlinedTextField(
                value = citySearchQuery,
                onValueChange = onCitySearchChange,
                label = { Text("🏙️ Search Cities", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search City",
                        tint = Color(0xFF764ba2)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF764ba2),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    cursorColor = Color(0xFF764ba2)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun StateItem(
    state: String,
    onStateSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onStateSelected(state) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationCity,
                contentDescription = null,
                tint = Color(0xFF667eea),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = state,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Searching amazing places...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorSection(error: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyStateSection(query: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No cities found for \"$query\"",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Text(
                text = "Try searching with a different name",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FamousPlacesSection(
    places: List<FamousPlace>,
    onCitySelected: (DestinationCity) -> Unit
) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌟",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Famous Destinations",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(places) { place ->
                        EnhancedFamousPlaceItem(
                            place = place,
                            onCitySelected = { onCitySelected(place.destination) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedFamousPlaceItem(
    place: FamousPlace,
    onCitySelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable { onCitySelected() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(Res.drawable.background_image),
                contentDescription = place.destination.city,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Rating Badge
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 12.sp
                    )
                    Text(
                        text = place.rating.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = place.destination.city,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = place.description,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CitiesListSection(
    cities: List<DestinationCity>,
    selectedCity: DestinationCity?,
    onCitySelected: (DestinationCity) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(cities) { city ->
            EnhancedCityItem(
                city = city,
                isSelected = city == selectedCity,
                onCitySelected = onCitySelected
            )
        }
    }
}

@Composable
private fun EnhancedCityItem(
    city: DestinationCity,
    isSelected: Boolean,
    onCitySelected: (DestinationCity) -> Unit
) {
    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 4.dp,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(animatedElevation, RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF667eea) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onCitySelected(city) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFF667eea).copy(alpha = 0.1f)
            else
                Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // City Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationCity,
                    contentDescription = "City",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // City Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city.city,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${city.state}, ${city.country}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // Selection Indicator
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFF667eea),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}