package org.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 🌍 Declare expect function (you implement it in androidMain)
expect fun generateTravelPdf()

@Composable
fun PdfScreen() {
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // 💠 Custom color for icon and button
    val lightBlue = Color(0xFF17ABF3)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ✈️ Light blue airplane icon
                    Icon(
                        imageVector = Icons.Default.FlightTakeoff,
                        contentDescription = "Travel",
                        tint = lightBlue,
                        modifier = Modifier.size(60.dp)
                    )

                    Text(
                        text = "Download Your Travel PDF",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Generate a shareable itinerary with all your booking info.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )

                    if (isLoading) {
                        CircularProgressIndicator(color = lightBlue)
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                coroutineScope.launch {
                                    generateTravelPdf()
                                    delay(1000) // simulate loading
                                    isLoading = false
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = lightBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Download PDF", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
