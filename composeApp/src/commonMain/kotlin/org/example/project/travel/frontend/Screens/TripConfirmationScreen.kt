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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TripConfirmationScreen(
    onConfirmTrip: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val primaryBlue = Color(23, 111, 243)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Trip Confirmation",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryBlue
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Destination Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Destination",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryBlue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Destination Image
//                Image(
//                    painter = painterResource("drawable/sample_destination.jpg"),
//                    contentDescription = "Destination Image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Crop
//                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Goa,India",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Text(
                    "7 Days, 6 Nights",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Budget Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Budget Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryBlue
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BudgetItem("Flight", "$50")
                BudgetItem("Hotel", "$60")
                BudgetItem("Activities", "$30")
                BudgetItem("Food & Drinks", "$20")
                BudgetItem("Transportation", "$100")
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color.LightGray
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total per person",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue
                    )
                    Text(
                        "$260",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Included Activities Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Included Activities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryBlue
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActivityImage("Beach Tour")
                    ActivityImage("Temple Visit")
                    ActivityImage("Spa Day")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Confirm Button
        Button(
            onClick = onConfirmTrip,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Text(
                "Confirm Booking",
                fontSize = 18.sp,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Back Button
        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Back to Search",
                color = primaryBlue,
                fontSize = 16.sp
            )
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