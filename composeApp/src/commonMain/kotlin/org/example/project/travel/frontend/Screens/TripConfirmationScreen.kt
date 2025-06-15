package org.example.project.travel.frontend.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TripConfirmationScreen(
    onConfirmTrip: () -> Unit = {},
    onBack: () -> Unit = {},
    onSupport: () -> Unit = {}
) {
    val primaryBlue = Color(23, 111, 243)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryBlue)
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Trip Summary",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }



        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
        // Destination Details
        SectionCard(
            title = "Destination Details",
            content = {
                Column {
                    Text(
                        "Goa, India",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "7 Days, 6 Nights (Dec 15 - Dec 21, 2024)",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Weather: 28°C, Mostly Sunny",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            icon = Icons.Default.LocationOn
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Transport Details
        SectionCard(
            title = "Transport",
            content = {
                Column {
                    TransportItem(
                        type = "Flight",
                        details = "Air India AI-123",
                        time = "Departure: Dec 15, 10:30 AM",
                        price = "$350"
                    )
                    BookingButton(text = "Book on MakeMyTrip")
                }
            },
            icon = Icons.Default.Flight
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Accommodation Details
        SectionCard(
            title = "Accommodation",
            content = {
                Column {
                    AccommodationItem(
                        name = "Taj Resort & Spa",
                        rating = "4.5 ★",
                        type = "Luxury Hotel",
                        price = "$200/night"
                    )
                    BookingButton(text = "Book on Booking.com")
                }
            },
            icon = Icons.Default.Hotel
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Activities & Dining
        SectionCard(
            title = "Activities & Dining",
            content = {
                Column {
                    ActivityItem(
                        name = "Beach Tour Package",
                        time = "Dec 16, 9:00 AM",
                        price = "$50"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ActivityItem(
                        name = "Temple Visit Tour",
                        time = "Dec 17, 10:00 AM",
                        price = "$30"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ActivityItem(
                        name = "Spa Day Package",
                        time = "Dec 18, 2:00 PM",
                        price = "$80"
                    )
                    BookingButton(text = "Book Activities on Viator")
                }
            },
            icon = Icons.Default.LocalActivity
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cost Breakdown
        SectionCard(
            title = "Cost Breakdown",
            content = {
                Column {
                    CostItem("Flights", "$350")
                    CostItem("Accommodation", "$1,200")
                    CostItem("Activities", "$160")
                    CostItem("Estimated Food & Drinks", "$300")
                    CostItem("Local Transport", "$100")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Estimate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "$2,110",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = primaryBlue
                        )
                    }
                }
            },
            icon = Icons.Default.AccountBalance
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Important Notes
        SectionCard(
            title = "Important Notes",
            content = {
                Column {
                    Text(
                        "• All bookings are handled on partner sites. We do not process payments.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "• Cancellation and refund policies are subject to partner sites.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        "• Prices are estimates and may vary at time of booking.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            icon = Icons.Default.Info
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Button(
            onClick = onConfirmTrip,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
        ) {
            Text("Proceed to Bookings", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                "Modify Itinerary",
                fontSize = 18.sp,
                color = primaryBlue
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onSupport,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Need Help? Contact Support",
                color = primaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(23, 111, 243),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(23, 111, 243)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun TransportItem(
    type: String,
    details: String,
    time: String,
    price: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Flight,
                contentDescription = type,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(type, fontWeight = FontWeight.Bold)
        }
        Text(details)
        Text(time, color = Color.Gray)
        Text(price, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AccommodationItem(
    name: String,
    rating: String,
    type: String,
    price: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Hotel,
                contentDescription = name,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(name, fontWeight = FontWeight.Bold)
        }
        Text("$rating • $type")
        Text(price, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ActivityItem(
    name: String,
    time: String,
    price: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when {
                            name.contains("Beach") -> Icons.Default.BeachAccess
                            name.contains("Temple") -> Icons.Default.AccountBalance
                            name.contains("Spa") -> Icons.Default.Spa
                            else -> Icons.Default.LocalActivity
                        },
                        contentDescription = name,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(name, fontWeight = FontWeight.Medium)
                }
                Text(time, color = Color.Gray, fontSize = 14.sp)
            }
        }
        Text(price, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CostItem(
    title: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (title) {
                    "Flights" -> Icons.Default.Flight
                    "Accommodation" -> Icons.Default.Hotel
                    "Activities" -> Icons.Default.LocalActivity
                    "Estimated Food & Drinks" -> Icons.Default.Restaurant
                    "Local Transport" -> Icons.Default.DirectionsCar
                    else -> Icons.Default.Payment
                },
                contentDescription = title,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title)
        }
        Text(amount)
    }
}

@Composable
private fun BookingButton(text: String) {
    OutlinedButton(
        onClick = { /* Handle external link */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(23, 111, 243)
        )
    ) {
        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = "Book",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
} 