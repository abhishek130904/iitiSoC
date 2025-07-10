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
import org.example.project.travel.frontEnd.pdf.TripSummary
import org.example.project.travel.frontEnd.pdf.generateTripSummaryPdf
import org.example.project.travel.frontEnd.pdf.saveTripSummaryPdfFile


@OptIn(ExperimentalResourceApi::class)
@Composable
fun TripConfirmationScreen(
    context: Any, // <-- added context parameter
    destination: String,
    dates: String,
    flightDetails: String,
    hotelDetails: String,
    activities: String,
    meals: String,
    costBreakdown: String,
    notes: String?,
    onHomeClick: () -> Unit = {},
    onMyTripsClick: () -> Unit = {}
) {
    val primaryBlue = Color(23, 111, 243)
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("tick.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)
    // Removed: val context = LocalContext.current

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
                // Download PDF Button
                Button(
                    onClick = {
                        val summary = TripSummary(
                            destination, dates, flightDetails, hotelDetails, activities, meals, costBreakdown, notes
                        )
                        val pdfBytes = generateTripSummaryPdf(summary)
                        saveTripSummaryPdfFile(context, pdfBytes, "TripSummary.pdf")
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Download Trip Summary (PDF)")
                }
            }
        }

        // Bottom Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp, start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 12.dp),
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
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Home", color = primaryBlue, fontWeight = FontWeight.Medium, fontSize = 13.sp)
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
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("My Trips", color = primaryBlue, fontWeight = FontWeight.Medium, fontSize = 13.sp)
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