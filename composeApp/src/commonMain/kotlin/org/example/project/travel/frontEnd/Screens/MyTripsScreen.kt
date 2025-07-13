package org.example.project.travel.frontEnd.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.example.project.travel.frontend.network.TravelApi
import org.example.project.travel.frontEnd.pdf.TripSummary
import org.example.project.travel.frontEnd.pdf.generateTripSummaryPdf
import org.example.project.travel.frontEnd.pdf.saveTripSummaryPdfFile

@Serializable
data class TripActivity(
    val time: String,
    val name: String,
    val description: String,
    val cost: Double
)

@Serializable
data class TripMeal(
    val type: String,
    val venue: String,
    val time: String,
    val cost: Double
)

@Serializable
data class TripItinerary(
    val id: Long,
    val userId: String,
    val cityName: String,
    val flightId: String,
    val hotelName: String,
    val activities: List<TripActivity>,
    val meals: List<TripMeal>,
    val notes: String? = null,
    val createdAt: String
)

@Composable
fun MyTripsScreen(userId: String, onHomeClick: () -> Unit) {
    var trips by remember { mutableStateOf<List<TripItinerary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showContent by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val primaryBlue = Color(23, 111, 243)
    val secondaryPurple = Color(0xFF667eea)

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2),
                        Color(0xFFF093FB)
                    )
                )
            )
    ) {
        // Floating background elements
        FloatingTravelElements()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Enhanced Header
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(600))
            ) {
                EnhancedHeader(
                    primaryBlue = primaryBlue,
                    onHomeClick = onHomeClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Content
            Box(modifier = Modifier.weight(1f)) {
                LaunchedEffect(userId) {
                    try {
                        trips = TravelApi.getMyTrips(userId)
                        isLoading = false
                    } catch (e: Exception) {
                        error = e.message
                        isLoading = false
                    }
                }

                when {
                    isLoading -> EnhancedLoadingState(primaryBlue)
                    error != null -> EnhancedErrorState(error!!, primaryBlue)
                    trips.isEmpty() -> EnhancedEmptyState(primaryBlue)
                    else -> EnhancedTripsList(
                        trips = trips,
                        context = context,
                        primaryBlue = primaryBlue,
                        showContent = showContent
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingTravelElements() {
    val travelIcons = listOf("✈️", "🏨", "🗺️", "🎒", "📸", "🌍")

    travelIcons.forEachIndexed { index, icon ->
        var animatedY by remember { mutableStateOf((0..100).random().toFloat()) }
        var animatedX by remember { mutableStateOf((0..100).random().toFloat()) }

        LaunchedEffect(Unit) {
            while (true) {
                animate(
                    initialValue = animatedY,
                    targetValue = if (animatedY > 50) 0f else 100f,
                    animationSpec = tween(
                        durationMillis = (4000..8000).random(),
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    animatedY = value
                }
                animate(
                    initialValue = animatedX,
                    targetValue = if (animatedX > 50) 0f else 100f,
                    animationSpec = tween(
                        durationMillis = (3000..6000).random(),
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
                    y = (animatedY * 6).dp
                )
        ) {
            Text(
                text = icon,
                fontSize = 24.sp,
                modifier = Modifier.alpha(0.3f)
            )
        }
    }
}

@Composable
private fun EnhancedHeader(
    primaryBlue: Color,
    onHomeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "My Trips",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
                Text(
                    text = "Your travel memories",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onHomeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBlue
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Home",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EnhancedLoadingState(primaryBlue: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = primaryBlue,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Loading your trips...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryBlue
                )
                Text(
                    "✈️ Gathering memories",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedErrorState(error: String, primaryBlue: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "😔",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Oops! Something went wrong",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    error,
                    fontSize = 14.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EnhancedEmptyState(primaryBlue: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🧳",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "No trips yet!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
                Text(
                    "Start planning your next adventure",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedTripsList(
    trips: List<TripItinerary>,
    context: android.content.Context,
    primaryBlue: Color,
    showContent: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(trips) { index, trip ->
            val animationDelay = if (showContent) index * 150 else 0

            EnhancedTripCard(
                trip = trip,
                context = context,
                primaryBlue = primaryBlue,
                animationDelay = animationDelay
            )
        }
    }
}

@Composable
private fun EnhancedTripCard(
    trip: TripItinerary,
    context: android.content.Context,
    primaryBlue: Color,
    animationDelay: Int
) {
    var visible by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        visible = true
    }

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
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .clickable { isExpanded = !isExpanded },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Trip Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = primaryBlue,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                trip.cityName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Date",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                trip.createdAt,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = primaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Quick Info
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickInfoChip(
                        icon = Icons.Default.Flight,
                        text = trip.flightId,
                        primaryBlue = primaryBlue
                    )
                    QuickInfoChip(
                        icon = Icons.Default.Hotel,
                        text = trip.hotelName,
                        primaryBlue = primaryBlue
                    )
                }

                // Expandable Content
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))

                        // Activities Section
                        if (trip.activities.isNotEmpty()) {
                            SectionHeader(
                                icon = Icons.Default.LocalActivity,
                                title = "Activities",
                                primaryBlue = primaryBlue
                            )

                            trip.activities.forEach { activity ->
                                ActivityItem(activity, primaryBlue)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Meals Section
                        if (trip.meals.isNotEmpty()) {
                            SectionHeader(
                                icon = Icons.Default.Restaurant,
                                title = "Meals",
                                primaryBlue = primaryBlue
                            )

                            trip.meals.forEach { meal ->
                                MealItem(meal, primaryBlue)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Notes Section
                        trip.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                            SectionHeader(
                                icon = Icons.Default.Notes,
                                title = "Notes",
                                primaryBlue = primaryBlue
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF8F9FA)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = notes,
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Download Button
                        EnhancedDownloadButton(
                            trip = trip,
                            context = context,
                            primaryBlue = primaryBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickInfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    primaryBlue: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = primaryBlue.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = primaryBlue,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    primaryBlue: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = primaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryBlue
        )
    }
}

@Composable
private fun ActivityItem(activity: TripActivity, primaryBlue: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        primaryBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activity.time,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = activity.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "₹${"%.2f".format(activity.cost)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )
        }
    }
}

@Composable
private fun MealItem(meal: TripMeal, primaryBlue: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        primaryBlue.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getMealEmoji(meal.type),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${meal.type} at ${meal.venue}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = meal.time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "₹${"%.2f".format(meal.cost)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )
        }
    }
}

@Composable
private fun EnhancedDownloadButton(
    trip: TripItinerary,
    context: android.content.Context,
    primaryBlue: Color
) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isPressed = true
            val summary = TripSummary(
                destination = trip.cityName,
                dates = trip.createdAt,
                flightDetails = trip.flightId,
                hotelDetails = trip.hotelName,
                activities = trip.activities.joinToString { it.name },
                meals = trip.meals.joinToString { it.type },
                costBreakdown = "",
                notes = trip.notes
            )
            val pdfBytes = generateTripSummaryPdf(summary)
            saveTripSummaryPdfFile(context, pdfBytes, "TripSummary_${trip.id}.pdf")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryBlue
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Download PDF Summary",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

private fun getMealEmoji(mealType: String): String {
    return when (mealType.lowercase()) {
        "breakfast" -> "🍳"
        "lunch" -> "🍽️"
        "dinner" -> "🍷"
        "snack" -> "🍪"
        else -> "🍴"
    }
}
