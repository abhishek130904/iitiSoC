package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.project.travel.frontEnd.viewModel.CityDetailsViewModel
import org.example.project.travel.frontend.model.Activity
import org.example.project.travel.frontend.navigation.RootComponent
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import org.example.project.travel.frontEnd.model.WeatherResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

interface CityDetailsScreenComponent {
    val viewModel: CityDetailsViewModel
    val cityName: String?
    fun onBack()
    fun onContinue()
}

class CityDetailsScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    cityId: String?,
    override val cityName: String? = null
) : CityDetailsScreenComponent, ComponentContext by componentContext {

    override val viewModel = CityDetailsViewModel(cityId, cityName)

    override fun onBack() {
        rootComponent.pop()
    }

    override fun onContinue() {
        rootComponent.navigateTo(org.example.project.travel.frontend.navigation.Screen.FlightSearch)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(component: CityDetailsScreenComponent) {
    val cityDetails by component.viewModel.cityDetails.collectAsState()
    val wikipediaData by component.viewModel.wikipediaData.collectAsState()
    val unsplashPhotos by component.viewModel.unsplashPhotos.collectAsState()
    val isLoading by component.viewModel.isLoading.collectAsState()
    val error by component.viewModel.error.collectAsState()
    val placeImages by component.viewModel.placeImages.collectAsState()
    var selectedPlace by remember { mutableStateOf<String?>(null) }

    // Weather state
    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    // or your existing client
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    val apiKey = "d03c8b8c415caa7fbccc6e045310b189" 

    LaunchedEffect(cityDetails?.city) {
        cityDetails?.let {
            weather = fetchWeatherForCity(httpClient, it.city, it.country, apiKey)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cityDetails?.city ?: component.cityName ?: "") },
                navigationIcon = {
                    IconButton(onClick = component::onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF176FF3),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { component.onContinue() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF176FF3))
            ){
                Text("Continue", color = Color.White, fontSize = 18.sp)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F4F8))
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error!!, color = Color.Red, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = component.viewModel::fetchCityDetails) {
                            Text("Retry")
                        }
                    }
                }
                cityDetails != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            weather?.let {
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFe0eafc))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(15.dp)
                                    ) {
                                        val iconUrl = "https://openweathermap.org/img/wn/${it.weather.firstOrNull()?.icon}@2x.png"
                                        KamelImage(
                                            resource = asyncPainterResource(data = iconUrl),
                                            contentDescription = it.weather.firstOrNull()?.description,
                                            modifier = Modifier.size(45.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(
                                                text = "${it.main.temp.roundToInt()}°C",
                                                fontSize = 25.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF176FF3)
                                            )
                                            Text(
                                                text = it.weather.firstOrNull()?.description?.replaceFirstChar { c -> c.uppercase() } ?: "",
                                                fontSize = 18.sp,
                                                color = Color(0xFF333333)
                                            )
                                            Text(
                                                text = it.name,
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                        item {
                            val images = unsplashPhotos?.results?.map { it.urls.regular to (it.alt_description ?: "") } ?: emptyList()
                            if (images.isNotEmpty()) {
                                CityImageCarousel(images)
                            }
                        }

                        item {
                            Text(
                                text = cityDetails!!.city,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )
                            Text(
                                text = "${cityDetails!!.state}, ${cityDetails!!.country}",
                                fontSize = 20.sp,
                                color = Color.Gray
                            )
                        }

                        item {
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    wikipediaData?.query?.pages?.values?.firstOrNull()?.let { page ->
                                        Text(
                                            text = "About ${page.title}",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF222222),
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        val aboutText = stripHtmlTags(page.extract)
                                        val wordCount = aboutText.trim().split("\\s+".toRegex()).size
                                        val finalText = if (wordCount < 15) {
                                            "${page.title} is more than just a place on the map—it's a journey into the heart of culture, tradition, and breathtaking landscapes. With every step, you'll uncover hidden stories, timeless architecture, and experiences that will stay with you forever. Whether you seek the thrill of discovery or the peace of scenic beauty, this place offers an unforgettable adventure waiting to unfold."
                                        } else {
                                            aboutText
                                        }

                                        Text(
                                            text = finalText,
                                            fontSize = 16.sp,
                                            lineHeight = 28.sp,
                                            color = Color(0xFF444444),
                                            letterSpacing = 0.25.sp,
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                        }


                            if (cityDetails!!.activities.isNotEmpty()) {
                                item {
                                    Text(
                                        "Top Tourist Places in ${cityDetails!!.city}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF333333)
                                    )
                                }
                                items(cityDetails!!.activities.filter { !it.properties.name.isNullOrBlank() }) { activity ->
                                    val placeName = activity.properties.name
                                    ActivityItem(
                                        activity = activity,
                                        isSelected = selectedPlace == placeName,
                                        imageUrl = placeImages[placeName]?.results?.firstOrNull()?.urls?.regular,
                                        onClick = {
                                            selectedPlace = if (selectedPlace == placeName) null else placeName
                                            if (selectedPlace != null && placeImages[placeName] == null) {
                                                component.viewModel.fetchPlaceImage(placeName)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(
    activity: Activity,
    isSelected: Boolean,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stripHtmlTags2(activity.properties.name.replaceFirstChar { it.uppercase() }),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stripHtmlTags2(activity.properties.kinds.replace(",", ", ").replaceFirstChar { it.uppercase() }),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))
                if (imageUrl != null) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        KamelImage(
                            resource = asyncPainterResource(data = imageUrl),
                            contentDescription = "Tourist place image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Image is for illustration only. Actual place may vary.",
                        color = Color(0xFFFF9800),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun CityImageCarousel(images: List<Pair<String, String>>) {
    var currentIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // Auto-slide every 10 seconds
    LaunchedEffect(images) {
        while (images.isNotEmpty()) {
            delay(10_000)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    val (imageUrl, _) = images.getOrNull(currentIndex) ?: ("" to "")

    Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
        if (imageUrl.isNotEmpty()) {
            KamelImage(
                resource = asyncPainterResource(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            // Left arrow
            if (images.size > 1) {
                IconButton(
                    onClick = {
                        currentIndex = if (currentIndex == 0) images.lastIndex else currentIndex - 1
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Filled.ArrowBackIos, contentDescription = "Previous", tint = Color.White)
                }
                // Right arrow
                IconButton(
                    onClick = {
                        currentIndex = (currentIndex + 1) % images.size
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Next", tint = Color.White)
                }
            }
        }
    }
    // Optionally, show indicators
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        images.forEachIndexed { idx, _ ->
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(if (idx == currentIndex) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (idx == currentIndex) Color(0xFF176FF3) else Color.LightGray)
            )
        }
    }
}

fun stripHtmlTags(html: String): String {
    return html.replace(Regex("<[^>]*>"), "").trim()
}

fun stripHtmlTags2(html: String): String {
    return html.replace(Regex("_[^_]*"), " ").trim()
}

suspend fun fetchWeatherForCity(httpClient: HttpClient, city: String, country: String, apiKey: String): WeatherResponse? {
    val url = "https://api.openweathermap.org/data/2.5/weather?q=${city},${country}&appid=${apiKey}&units=metric"
    return try {
        httpClient.get(url).body<WeatherResponse>()
    } catch (e: Exception) {
        null
    }
}


