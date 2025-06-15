package org.example.project.travel.frontEnd.Screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.background_image

@Composable
fun DestiSelect() {
    val blue = Color(0xFF176FF3)
    val white = Color.White

    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(Res.drawable.background_image),
            contentDescription = "Destination Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Translucent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Let’s plan your perfect trip!",
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = white,
                modifier = Modifier.padding(bottom = 32.dp)
            )

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
                subtitle2 = "Step into the past - Explore history & heritage",
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.5f),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        onClick = onClick,
        interactionSource = interactionSource
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icon inside rounded box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.8f), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = textColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = subtitle1,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.8f)
                )
                Text(
                    text = subtitle2,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
