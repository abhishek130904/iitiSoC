package org.example.project.travel.frontend.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.sp





@Composable
fun TopActivity() {
    val AppBlue = Color(red = 23, green = 111, blue = 243)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Discover Goa!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppBlue,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TravelCategoryCard(
                title = "Top Activities",
                cities = listOf("Water Skiing", "Parasailing", "Kite Surfing", "Cruise", "Jet Skiing"),
                onItemClick = { city ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Selected: $city")
                    }
                }
            )

            TravelCategoryCard(
                title = "Famous Sightseeing",
                cities = listOf("Anjuna Beach", "Baga Beach", "Dudhsagar Waterfalls", "Calangute Beach", "Chapora Fort"),
                onItemClick = { city ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Selected: $city")
                    }
                }
            )

            TravelCategoryCard(
                title = "Famous Food",
                cities = listOf("Goan Fish Curry", "Prawn Balchao", "Bebinca", "Goan Nevri"),
                onItemClick = { city ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Selected: $city")
                    }
                }
            )
        }
    }
}

@Composable
fun TravelCategoryCard(
    title: String,
    cities: List<String>,
    onItemClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCities = cities.filter { it.contains(searchQuery, ignoreCase = true) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppBlue,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = AppBlue)
                },
                placeholder = {
                    Text("Search...", color = AppBlue.copy(alpha = 0.6f))
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppBlue,
                    unfocusedTextColor = AppBlue,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AppBlue,
                    unfocusedBorderColor = AppBlue,
                    cursorColor = AppBlue
                )
            )

            if (filteredCities.isEmpty()) {
                Text(
                    text = "No results found",
                    color = AppBlue.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredCities.forEach { city ->
                        var isClicked by remember { mutableStateOf(false) }
                        val backgroundColor by animateColorAsState(
                            if (isClicked) AppBlue.copy(alpha = 0.8f) else AppBlue,
                            label = ""
                        )

                        Button(
                            onClick = {
                                isClicked = true
                                onItemClick(city)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(city)
                        }
                    }
                }
            }
        }
    }
}
