package org.example.project.travel.frontEnd.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import com.airbnb.lottie.compose.*
import org.example.project.travel.frontEnd.pdf.TripSummary
import org.example.project.travel.frontEnd.pdf.generateTripSummaryPdf
import org.example.project.travel.frontEnd.pdf.saveTripSummaryPdfFile
import kotlinx.coroutines.delay
import org.example.project.travel.frontend.auth.getCurrentFirebaseUserUid
import org.example.project.travel.frontend.navigation.Screen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TripConfirmationScreen(
    context: Any,
    destination: String,
    dates: String,
    flightDetails: String,
    hotelDetails: String,
    activities: String,
    meals: String,
    costBreakdown: String,
    notes: String?,
    onHomeClick: () -> Unit = {},
    onMyTripsClick: (() -> Unit)? = null
) {
    val primaryBlue = Color(23, 111, 243)
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("tick.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    var showContent by remember { mutableStateOf(false) }
    var showBottomBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showContent = true
        delay(1500)
        showBottomBar = true
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
        // Floating particles background effect
        FloatingParticles()

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Main Confirmation Card
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(800))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(20.dp, RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.98f)
                    )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                        // Success Animation
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                            modifier = Modifier.size(200.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Success Message
                        Text(
                            text = "🎉 Trip Confirmed!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Your amazing journey awaits!",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Trip Summary Card
                        TripSummaryCard(
                            destination = destination,
                            dates = dates,
                            primaryBlue = primaryBlue
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                // Download PDF Button
                        EnhancedDownloadButton(
                            primaryBlue = primaryBlue,
                    onClick = {
                        val summary = TripSummary(
                                    destination, dates, flightDetails, hotelDetails,
                                    activities, meals, costBreakdown, notes
                        )
                        val pdfBytes = generateTripSummaryPdf(summary)
                        saveTripSummaryPdfFile(context, pdfBytes, "TripSummary.pdf")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Enhanced Bottom Navigation
        AnimatedVisibility(
            visible = showBottomBar,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
                ) {
            EnhancedBottomBar(
                primaryBlue = primaryBlue,
                onHomeClick = onHomeClick,
                onMyTripsClick = {
                    val userId = getCurrentFirebaseUserUid()
                    if (userId != null && onMyTripsClick != null) {
                        onMyTripsClick()
                    }
                }
            )
        }
    }
}

@Composable
private fun FloatingParticles() {
    val particles = remember {
        List(15) {
            Particle(
                x = (0..100).random(),
                y = (0..100).random(),
                size = (4..12).random()
            )
                }
            }

    particles.forEach { particle ->
        var animatedY by remember { mutableStateOf(particle.y.toFloat()) }

        LaunchedEffect(Unit) {
            while (true) {
                animate(
                    initialValue = animatedY,
                    targetValue = if (animatedY > 50) 0f else 100f,
                    animationSpec = tween(
                        durationMillis = (3000..6000).random(),
                        easing = LinearEasing
                    )
                ) { value, _ ->
                    animatedY = value
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .offset(
                    x = (particle.x * 4).dp,
                    y = (animatedY * 8).dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(particle.size.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
private fun TripSummaryCard(
    destination: String,
    dates: String,
    primaryBlue: Color
) {
    Card(
            modifier = Modifier
                .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.FlightTakeoff,
                    contentDescription = "Trip",
                    tint = primaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Trip Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TripDetailRow(
                icon = Icons.Default.LocationOn,
                label = "Destination",
                value = destination,
                primaryBlue = primaryBlue
            )

            TripDetailRow(
                icon = Icons.Default.DateRange,
                label = "Travel Dates",
                value = dates,
                primaryBlue = primaryBlue
            )

            TripDetailRow(
                icon = Icons.Default.CheckCircle,
                label = "Status",
                value = "Confirmed ✅",
                primaryBlue = primaryBlue
            )
        }
    }
}

@Composable
private fun TripDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    primaryBlue: Color
) {
    Row(
                    modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
            imageVector = icon,
            contentDescription = label,
                        tint = primaryBlue,
            modifier = Modifier.size(20.dp)
                    )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EnhancedDownloadButton(
    primaryBlue: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryBlue
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Download Trip Summary",
                color = Color.White,
                fontSize = 16.sp,
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

@Composable
private fun EnhancedBottomBar(
    primaryBlue: Color,
    onHomeClick: () -> Unit,
    onMyTripsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Button
            EnhancedNavButton(
                icon = Icons.Default.Home,
                label = "Home",
                primaryBlue = primaryBlue,
                onClick = onHomeClick
            )

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

                // My Trips Button
            EnhancedNavButton(
                icon = Icons.Default.ListAlt,
                label = "My Trips",
                primaryBlue = primaryBlue,
                onClick = onMyTripsClick
            )
        }
    }
}

@Composable
private fun EnhancedNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    primaryBlue: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
            .clickable {
                isPressed = true
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = if (isPressed) {
                        Brush.radialGradient(
                            colors = listOf(
                                primaryBlue.copy(alpha = 0.2f),
                                primaryBlue.copy(alpha = 0.1f)
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent
                            )
                        )
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
                ) {
                    Icon(
                imageVector = icon,
                contentDescription = label,
                        tint = primaryBlue,
                modifier = Modifier.size(24.dp)
                    )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = primaryBlue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
                }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

// Helper data class for particles
private data class Particle(
    val x: Int,
    val y: Int,
    val size: Int
)

@Composable
private fun BudgetItem(title: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            amount,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ActivityImage(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(4.dp)
        )
    }
} 