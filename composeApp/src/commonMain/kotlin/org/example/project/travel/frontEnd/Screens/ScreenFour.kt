package org.example.project.travel.frontEnd.Screens
import org.example.project.travel.frontEnd.Screens.ScreenFour

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess // Added import
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenFour() {
    val blue = Color(0xFF176FF3)
    val white = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "What do you prefer?",
            fontStyle = FontStyle.Italic,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = blue
        )

        Spacer(modifier = Modifier.height(32.dp))

        PreferenceButton(
            title = "Beach",
            subtitle1 = "Best during Nov–Mar",
            subtitle2 = "Sunny weather, water sports & Beach shacks",
            icon = Icons.Default.BeachAccess,
            onClick = { println("Beach clicked") },
            color = blue,
            textColor = white
        )

        PreferenceButton(
            title = "Mountains",
            subtitle1 = "Best during Sep–Oct",
            subtitle2 = "Hiking, panoramic views & peaceful retreats",
            icon = Icons.Default.Terrain,
            onClick = { println("Mountains clicked") },
            color = blue,
            textColor = white
        )

        PreferenceButton(
            title = "Hill Station",
            subtitle1 = "Best during Sep–May",
            subtitle2 = "Cool getaways & scenic views",
            icon = Icons.Default.AcUnit,
            onClick = { println("Hill Station clicked") },
            color = blue,
            textColor = white
        )

        PreferenceButton(
            title = "Historical Place",
            subtitle1 = "Best during Nov–Feb",
            subtitle2 = "Step into the past-Explore history & heritage",
            icon = Icons.Default.AccountBalance,
            onClick = { println("Historical Place clicked") },
            color = blue,
            textColor = white
        )

        PreferenceButton(
            title = "Pilgrimage",
            subtitle1 = "Best during Apr–Jul",
            subtitle2 = "Spiritual & peaceful journeys",
            icon = Icons.Default.SelfImprovement,
            onClick = { println("Pilgrimage clicked") },
            color = blue,
            textColor = white
        )
    }
}


@Composable
fun PreferenceButton(
    title: String,
    subtitle1: String,
    subtitle2: String,
    icon: ImageVector,
    onClick: () -> Unit,
    color: Color,
    textColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // Align content to the start with controlled spacing
            modifier = Modifier.fillMaxWidth() // Ensure the Row takes the full width of the button
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = textColor,
                modifier = Modifier
                    .padding(end = 12.dp) // Keep the spacing between icon and text
                    .size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f) // Let the Column take remaining space but not stretch too far
            ) {
                Text(
                    text = title,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp // Ensure title size is consistent
                )
                Text(
                    text = subtitle1,
                    fontSize = 12.sp,
                    color = textColor,
                    lineHeight = 16.sp // Add line height for better readability
                )
                Text(
                    text = subtitle2,
                    fontSize = 12.sp,
                    color = textColor,
                    lineHeight = 16.sp // Add line height for better readability
                )
            }
        }
    }
}