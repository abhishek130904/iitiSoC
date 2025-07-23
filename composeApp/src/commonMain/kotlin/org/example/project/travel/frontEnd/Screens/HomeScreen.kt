package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import kotlinx.coroutines.delay
import org.example.project.travel.frontend.auth.UserProfile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.*
import androidx.compose.ui.platform.LocalContext
import org.example.project.travel.frontEnd.notification.showTestNotification

interface HomeScreenComponent {
    val userName: String // Fetched from authService or user profile
    fun onNavigateToCitySearch()
}

// Category data class for better organization
data class TravelCategory(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val bgColor: Color,
    val description: String,
    val popularDestinations: List<String>
)

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCitySearch: () -> Unit,
    onProfileClick: () -> Unit,
    onCategoryClick: (TravelCategory) -> Unit = {}
) {
    val blue = Color(0xFF176FF3)
    val white = Color.White
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Enhanced categories with more data
    val categories = listOf(
        TravelCategory(
            title = "Beaches",
            icon = Icons.Default.BeachAccess,
            bgColor = Color(0xFFE0F7FA),
            description = "Discover pristine beaches and coastal paradises",
            popularDestinations = listOf(
                "Panjim", "Vasco da Gama", "Sancoale", "Puducherry", "Alibag", "Gokarna"
            )
        ),
        TravelCategory(
            title = "Mountains",
            icon = Icons.Default.Terrain,
            bgColor = Color(0xFFE8F5E9),
            description = "Explore majestic mountains and scenic trails",
            popularDestinations = listOf(
                "Manali", "Shimla", "Mussoorie", "Naini Tal", "Darjiling", "Dehra Dun", "Leh", "Kargil", "Munnar"
            )
        ),
        TravelCategory(
            title = "Hill Stations",
            icon = Icons.Default.Landscape,
            bgColor = Color(0xFFFFF3E0),
            description = "Escape to cool hill stations and mountain retreats",
            popularDestinations = listOf(
                "Ooty", "Munnar", "Shimla", "Darjiling", "Mussoorie", "Kodaikanal", "Lonavla", "Dhramsala", "Pithoragarhr"
            )
        ),
        TravelCategory(
            title = "Forests",
            icon = Icons.Default.Nature,
            bgColor = Color(0xFFEDE7F6),
            description = "Immerse in nature and wildlife sanctuaries",
            popularDestinations = listOf(
                "Ramnagar", "Bandipur", "Kanhan", "Nahan", "Pauri", "Chandrapura", "Gosaba"
            )
        ),
        TravelCategory(
            title = "Historical",
            icon = Icons.Default.AccountBalance,
            bgColor = Color(0xFFFFEBEE),
            description = "Journey through ancient monuments and heritage sites",
            popularDestinations = listOf(
                "Pune", "Aurangabad", "Fatehpur Sikri", "Khajuraho", "Jaipur", "Agra", "Delhi", "Mysuru", "Gwalior", "Bidar"
            )
        ),
        TravelCategory(
            title = "Wildlife",
            icon = Icons.Default.Pets,
            bgColor = Color(0xFFE1F5FE),
            description = "Experience wildlife adventures and safaris",
            popularDestinations = listOf(
                "Dandeli", "Umaria", "Bokakhat", "Ramnagar",  "Mandla", "Seoni", "Chandrapura"
            )
        ),
        TravelCategory(
            title = "Temples",
            icon = Icons.Default.TempleBuddhist,
            bgColor = Color(0xFFFFFDE7),
            description = "Visit sacred temples and spiritual destinations",
            popularDestinations = listOf(
                "Varanasi", "Tirupati", "Shirdi", "Amritsar", "Madurai", "Puri", "Rameswaram", "Ujjain", "Dwarka", "Kanchipuram"
            )
        ),
        TravelCategory(
            title = "Adventure",
            icon = Icons.Default.DirectionsBike,
            bgColor = Color(0xFFE8EAF6),
            description = "Thrilling adventures and outdoor activities",
            popularDestinations = listOf(
                "Rishikesh", "Manali", "Jaisalmer", "Munnar", "Dandeli", "Gulmarg", "Ranikhet", "Leh"
            )
        )
    )

    val imageResourcesWithLabels = listOf(
        Pair(Res.drawable.tajmahal, "Taj Mahal"),
        Pair(Res.drawable.beaches, "Goan Beach"),
        Pair(Res.drawable.temple, "Temple"),
        Pair(Res.drawable.hillstation, "Hill Station")
    )

    val totalItems = Int.MAX_VALUE
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = totalItems / 2)

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500L)
            val nextIndex = listState.firstVisibleItemIndex + 1
            listState.animateScrollToItem(nextIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome, Traveller",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = blue
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = blue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = white,
                    titleContentColor = blue
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Attractive "Create a Perfect Trip for You" Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create a Perfect Trip for You!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Let us craft a personalized travel experience tailored to your preferences.",
                        fontSize = 14.sp,
                        color = Color(0xFF455A64),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { onNavigateToCitySearch() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(40.dp)
                    ) {
                        Text("Get Started", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Text(
                text = "Where to go next?",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = blue
            )

            // Functional Categories Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                (0..1).forEach { colIndex ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        categories.drop(colIndex * 4).take(4).forEach { category ->
                            FunctionalCategoryCard(
                                category = category,
                                blue = blue,
                                onClick = { onCategoryClick(category) }
                            )
                        }
                    }
                }
            }

            Text(
                text = "Popular Destinations",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = blue
            )

            InfiniteCarousel(items = imageResourcesWithLabels, listState = listState)
        }
    }
}

@Composable
fun FunctionalCategoryCard(
    category: TravelCategory,
    blue: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = category.bgColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                tint = blue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = category.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InfiniteCarousel(
    items: List<Pair<DrawableResource, String>>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(18.dp)
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        items(count = Int.MAX_VALUE) { index ->
            val actualIndex = index % items.size
            val (imageRes, label) = items[actualIndex]
            Card(
                shape = cardShape,
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Box {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(cardShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                )
                            )
                    )
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}
