import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

val AppBlue = Color(red = 23, green = 111, blue = 243)

@Composable
fun TravelCategoryScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            TravelCategory(
                title = "Beach",
                cities = listOf("Goa", "Gokarna", "Pondicherry", "Alibaug", "Kovalam"),
                onCityClick = { city ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("You selected $city")
                    }
                }
            )

            TravelCategory(
                title = "Mountain",
                cities = listOf("Manali", "Leh", "Spiti", "Auli", "Tawang"),
                onCityClick = { city -> coroutineScope.launch { snackbarHostState.showSnackbar("Selected $city") } }
            )

            TravelCategory(
                title = "Hill Station",
                cities = listOf("Ooty", "Munnar", "Shimla", "Darjeeling", "Mussoorie"),
                onCityClick = { city -> coroutineScope.launch { snackbarHostState.showSnackbar("Selected $city") } }
            )

            TravelCategory(
                title = "Historical Place",
                cities = listOf("Hampi", "Ajanta", "Fatehpur Sikri", "Kumbhalgarh", "Khajuraho"),
                onCityClick = { city -> coroutineScope.launch { snackbarHostState.showSnackbar("Selected $city") } }
            )

            TravelCategory(
                title = "Pilgrimage",
                cities = listOf("Varanasi", "Rishikesh", "Tirupati", "Shirdi", "Amritsar"),
                onCityClick = { city -> coroutineScope.launch { snackbarHostState.showSnackbar("Selected $city") } }
            )
        }
    }
}

@Composable
fun TravelCategory(
    title: String,
    cities: List<String>,
    onCityClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCities = cities.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppBlue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...", color = AppBlue.copy(alpha = 0.6f)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = AppBlue)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppBlue,
                unfocusedBorderColor = AppBlue,
                focusedTextColor = AppBlue,
                unfocusedTextColor = AppBlue,
                cursorColor = AppBlue
            )
        )

        AnimatedVisibility(visible = filteredCities.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                filteredCities.forEach { city ->
                    Button(
                        onClick = { onCityClick(city) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = city)
                    }
                }
            }
        }

        if (filteredCities.isEmpty()) {
            Text(
                text = "No results found.",
                color = AppBlue.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
}
