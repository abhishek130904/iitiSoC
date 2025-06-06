import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TravelCategoriesScreen() {
    val columnTitles = listOf("Beach", "Mountain", "Hill Station", "Historical Place", "Pilgrimage")

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        columnTitles.forEach { title ->
            CategoryColumn(title = title)
        }
    }
}

@Composable
fun CategoryColumn(title: String) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .background(Color(0xFFE0F7FA), shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            color = Color(0xFF006064),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Placeholder for adding cities
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(vertical = 4.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "City ${it + 1}", color = Color.Gray)
            }
        }
    }
}
