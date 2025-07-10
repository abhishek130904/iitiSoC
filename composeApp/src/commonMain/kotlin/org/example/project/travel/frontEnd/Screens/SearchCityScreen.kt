package org.example.project.travel.frontend.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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

// Data for famous places
interface CitySearchScreenComponent {
    fun onCitySelected(city: org.example.project.travel.frontend.model.DestinationCity)
    fun onBack()
}

class CitySearchScreenComponentImpl(
    private val componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : CitySearchScreenComponent, ComponentContext by componentContext {

    override fun onCitySelected(city: org.example.project.travel.frontend.model.DestinationCity) {
        rootComponent.navigateTo(Screen.CityDetails(city.id.toString(), city.city))
    }

    override fun onBack() {
        rootComponent.pop()
    }
}

data class FamousPlace(
    val destination: DestinationCity,
    val imageRes: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(component: CitySearchScreenComponent, viewModel: CitySearchViewModel<DestinationCity>) {
    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<DestinationCity?>(null) }

    LaunchedEffect(cities) {
        if (cities.isNotEmpty() && selectedCity == null) {
            selectedCity = cities.first()
        }
    }

    val famousPlaces = listOf(
        FamousPlace(DestinationCity(id = 800, city = "Panjim-Goa", state = "Goa", country = "India", cityCode = 1260607), "drawable/beach.jpg"),
        FamousPlace(DestinationCity(id = 1643, city = "Jaipur", state = "Rajasthan", country = "India", cityCode = 1269515), "drawable/fort.jpg"),
        FamousPlace(DestinationCity(id = 2598, city = "Agra", state = "Uttar Pradesh", country = "India", cityCode = 1279259), "drawable/tajmahal.png"),
        FamousPlace(DestinationCity(id = 76, city = "Varanasi", state = "Uttar Pradesh", country = "India", cityCode = 1253405), "drawable/temple.jpg")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                        viewModel.searchCities(it)

                },
                label = { Text("Search for a city") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    error != null -> Text(
                        text = error ?: "An unknown error occurred.",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    searchQuery.isEmpty() -> FamousPlacesList(
                        places = famousPlaces,
                        onCitySelected = {
                            selectedCity = it
                            component.onCitySelected(it)
                        }
                    )
                    cities.isEmpty() && searchQuery.isNotEmpty() -> Text(
                        "No cities found for \"$searchQuery\"",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(cities) { city ->
                            CityItem(
                                city = city,
                                isSelected = city == selectedCity,
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
    }
}

@Composable
fun FamousPlacesList(
    places: List<FamousPlace>,
    onCitySelected: (DestinationCity) -> Unit
) {
    Column {
        Text(
            "Famous Places in India",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(places) { place ->
                FamousPlaceItem(place = place, onCitySelected = { onCitySelected(place.destination) })
            }
        }
    }
}

@Composable
fun FamousPlaceItem(place: FamousPlace, onCitySelected: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable(onClick = onCitySelected),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.background_image),
                contentDescription = place.destination.city,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Text(
                text = place.destination.city,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun CityItem(
    city: DestinationCity,
    isSelected: Boolean,
    onCitySelected: (DestinationCity) -> Unit
) {
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