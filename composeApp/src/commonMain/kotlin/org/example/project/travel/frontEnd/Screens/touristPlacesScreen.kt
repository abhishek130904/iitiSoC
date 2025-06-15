package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TravelAppScreen(userName: String) {
    val blue = Color(0xFF176FF3)
    val white = Color.White

    val categories = listOf(
        Triple("Beaches", Icons.Default.BeachAccess, Color(0xFFE0F7FA)),
        Triple("Mountains", Icons.Default.Terrain, Color(0xFFE8F5E9)),
        Triple("Hill Stations", Icons.Default.Landscape, Color(0xFFFFF3E0)),
        Triple("Forests", Icons.Default.Nature, Color(0xFFEDE7F6)),
        Triple("Historical", Icons.Default.AccountBalance, Color(0xFFFFEBEE)),
        Triple("Wildlife", Icons.Default.Pets, Color(0xFFE1F5FE)),
        Triple("Temples", Icons.Default.TempleBuddhist, Color(0xFFFFFDE7)),
        Triple("Adventure", Icons.Default.DirectionsBike, Color(0xFFE8EAF6))
    )

    val imageResources = listOf(
        Res.drawable.tajmahal,
        Res.drawable.beaches,
        Res.drawable.temple,
        Res.drawable.hillstation
    )


    val extendedImageList = remember { imageResources + imageResources }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        val initialIndex = imageResources.size
        listState.scrollToItem(initialIndex)
        while (true) {
            delay(2000L)
            val nextIndex = listState.firstVisibleItemIndex + 1
            listState.animateScrollToItem(nextIndex)


            if (nextIndex >= extendedImageList.lastIndex) {
                listState.scrollToItem(imageResources.size)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Welcome, $userName",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = blue
            )

            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = blue
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                (0..1).forEach { colIndex ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.drop(colIndex * 4).take(4).forEach { (title, icon, bgColor) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = bgColor)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title,
                                        tint = blue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                }
                            }
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


            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(extendedImageList) { imageRes ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))


            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { println("Create a Trip clicked") },
                    colors = ButtonDefaults.buttonColors(containerColor = blue),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TravelExplore, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Create Trip", color = Color.White)
                    }
                }

                Button(
                    onClick = { println("Previous Trips clicked") },
                    colors = ButtonDefaults.buttonColors(containerColor = blue),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Previous Trips", color = Color.White)
                    }
                }
            }
        }
    }
}
