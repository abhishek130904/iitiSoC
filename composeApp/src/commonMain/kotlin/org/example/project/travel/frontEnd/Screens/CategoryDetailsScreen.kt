package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.project.travel.frontend.network.TravelApi
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsScreen(
    categoryTitle: String,
    categoryDescription: String,
    destinations: List<String>,
    onDestinationClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    // --- Category image state (with cache) ---
    var categoryImageUrl by rememberSaveable(categoryTitle) { mutableStateOf<String?>(null) }
    var categoryImageLoading by rememberSaveable(categoryTitle) { mutableStateOf(false) }
    var categoryImageError by rememberSaveable(categoryTitle) { mutableStateOf<String?>(null) }

    LaunchedEffect(categoryTitle) {
        if (categoryImageUrl == null && !categoryImageLoading) {
            categoryImageLoading = true
            categoryImageError = null
            try {
                val response = TravelApi.getCityPhotos(categoryTitle)
                categoryImageUrl = response.results.firstOrNull()?.urls?.regular
            } catch (e: Exception) {
                categoryImageError = e.message
            } finally {
                categoryImageLoading = false
            }
        }
    }
    // --- End category image state ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF176FF3)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF176FF3)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF176FF3)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Category Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- Category Unsplash image ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(bottom = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                categoryImageLoading -> CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                categoryImageError != null -> Text("Image unavailable", color = Color.Gray, fontSize = 12.sp)
                                categoryImageUrl != null -> KamelImage(
                                    resource = asyncPainterResource(data = categoryImageUrl!!),
                                    contentDescription = "$categoryTitle image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        // --- End Category Unsplash image ---
                        Text(
                            text = categoryTitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = categoryDescription,
                            fontSize = 16.sp,
                            color = Color(0xFF455A64),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Popular Destinations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF176FF3)
                )
            }
            items(destinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = { onDestinationClick(destination) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DestinationCard(
    destination: String,
    onClick: () -> Unit
) {
    // Unsplash image state for the destination (with cache)
    var imageUrl by rememberSaveable(destination) { mutableStateOf<String?>(null) }
    var isLoading by rememberSaveable(destination) { mutableStateOf(false) }
    var error by rememberSaveable(destination) { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(destination) {
        if (imageUrl == null && !isLoading) {
            isLoading = true
            error = null
            try {
                val response = TravelApi.getCityPhotos(destination)
                imageUrl = response.results.firstOrNull()?.urls?.regular
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Destination Image (Unsplash)
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            } else if (imageUrl != null) {
                KamelImage(
                    resource = asyncPainterResource(data = imageUrl!!),
                    contentDescription = "$destination image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback icon if image not available
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF176FF3),
                                    Color(0xFF1976D2)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Destination",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Destination Name
            Text(
                text = destination,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View details",
                tint = Color(0xFF176FF3),
                modifier = Modifier.size(20.dp)
            )
        }
    }
} 