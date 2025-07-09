package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import com.airbnb.lottie.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TripConfirmationScreen(
    onHomeClick: () -> Unit = {},
    onMyTripsClick: () -> Unit = {}
) {
    val primaryBlue = Color(23, 111, 243)
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("tick.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFFe0eafc), Color(0xFFcfdef3))))
    ) {
        // Centered Card with Confirmation Message and Animation
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your trip is confirmed!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(180.dp)
                )
            }
        }

        // Bottom Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onHomeClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = primaryBlue,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Home", color = primaryBlue, fontWeight = FontWeight.Medium)
                }
                // My Trips Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onMyTripsClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "My Trips",
                        tint = primaryBlue,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("My Trips", color = primaryBlue, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

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
//        Image(
//            painter = painterResource("drawable/activity_placeholder.jpg"),
//            contentDescription = title,
//            modifier = Modifier
//                .size(80.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            title,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(4.dp)
        )
    }
} 