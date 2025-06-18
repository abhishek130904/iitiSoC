package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val pages = listOf(
        OnboardingPage("Find your perfect place to stay!", "Explore stays and destinations with ease.", "welcome.json"),
        OnboardingPage("Book appointment in easiest way!", "Smooth planning and booking at your fingertips.", "travel2.json"),
        OnboardingPage("Let's discover & enjoy the world!", "Get ready for your next unforgettable journey.", "loading.json")
    )

    var pageIndex by remember { mutableStateOf(0) }
    val blue = Color(0xFF176FF3)

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(blue, Color.White)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(24.dp))

            // Logo (only on the first screen)
            if (pageIndex == 0) {
                Text(
                    text = "GoTour",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            val composition by rememberLottieComposition(
                LottieCompositionSpec.Asset(pages[pageIndex].animationAsset)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 1
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = pages[pageIndex].title,
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pages[pageIndex].description,
                fontSize = 15.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Dots Indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.indices.forEach { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (i == pageIndex) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (i == pageIndex) Color.White else Color.LightGray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (pageIndex < pages.lastIndex) {
                        pageIndex++
                    } else {
                        onNavigateToSignIn()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (pageIndex == pages.lastIndex) "Get Started" else "Continue",
                    color = blue
                )
            }

            TextButton(onClick = onFinished) {
                Text("Skip", color = Color.White)
            }
        }
    }
}



data class OnboardingPage(
    val title: String,
    val description: String,
    val animationAsset: String
)