package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel.network.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.project.travel.frontend.model.DestinationCity
import kotlinx.coroutines.launch
import java.net.URLEncoder
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Composable
fun StateScreen(
    stateName: String,
    onCitySelected: (cityId: String, cityName: String) -> Unit
) {
    val topCities = remember(stateName) {
        when (stateName) {
            "Andhra Pradesh" -> listOf(
                DestinationCity(11, "Visakhapatnam", "Andhra Pradesh", "India", 301),
                DestinationCity(12, "Vijayawada", "Andhra Pradesh", "India", 302),
                DestinationCity(13, "Tirupati", "Andhra Pradesh", "India", 303),
                DestinationCity(14, "Amaravati", "Andhra Pradesh", "India", 304),
                DestinationCity(15, "Kurnool", "Andhra Pradesh", "India", 305)
            )
            "Arunachal Pradesh" -> listOf(
                DestinationCity(16, "Itanagar", "Arunachal Pradesh", "India", 401),
                DestinationCity(17, "Tawang", "Arunachal Pradesh", "India", 402),
                DestinationCity(18, "Ziro", "Arunachal Pradesh", "India", 403)
            )
            "Assam" -> listOf(
                DestinationCity(19, "Guwahati", "Assam", "India", 501),
                DestinationCity(20, "Dibrugarh", "Assam", "India", 502),
                DestinationCity(21, "Jorhat", "Assam", "India", 503),
                DestinationCity(22, "Silchar", "Assam", "India", 504)
            )
            "Bihar" -> listOf(
                DestinationCity(1, "Patna", "Bihar", "India", 101),
                DestinationCity(2, "Gaya", "Bihar", "India", 102),
                DestinationCity(3, "Bodh Gaya", "Bihar", "India", 103),
                DestinationCity(4, "Nalanda", "Bihar", "India", 104),
                DestinationCity(5, "Rajgir", "Bihar", "India", 105)
            )
            "Chhattisgarh" -> listOf(
                DestinationCity(23, "Raipur", "Chhattisgarh", "India", 601),
                DestinationCity(24, "Bilaspur", "Chhattisgarh", "India", 602),
                DestinationCity(25, "Jagdalpur", "Chhattisgarh", "India", 603)
            )
            "Goa" -> listOf(
                DestinationCity(26, "Panjim", "Goa", "India", 701),
                DestinationCity(27, "Margao", "Goa", "India", 702),
                DestinationCity(28, "Vasco da Gama", "Goa", "India", 703)
            )
            "Gujarat" -> listOf(
                DestinationCity(29, "Ahmedabad", "Gujarat", "India", 801),
                DestinationCity(30, "Surat", "Gujarat", "India", 802),
                DestinationCity(31, "Vadodara", "Gujarat", "India", 803),
                DestinationCity(32, "Rajkot", "Gujarat", "India", 804)
            )
            "Haryana" -> listOf(
                DestinationCity(33, "Gurgaon", "Haryana", "India", 901),
                DestinationCity(34, "Faridabad", "Haryana", "India", 902),
                DestinationCity(35, "Panipat", "Haryana", "India", 903)
            )
            "Himachal Pradesh" -> listOf(
                DestinationCity(36, "Shimla", "Himachal Pradesh", "India", 1001),
                DestinationCity(37, "Manali", "Himachal Pradesh", "India", 1002),
                DestinationCity(38, "Dharamshala", "Himachal Pradesh", "India", 1003)
            )
            "Jharkhand" -> listOf(
                DestinationCity(39, "Ranchi", "Jharkhand", "India", 1101),
                DestinationCity(40, "Jamshedpur", "Jharkhand", "India", 1102),
                DestinationCity(41, "Dhanbad", "Jharkhand", "India", 1103)
            )
            "Karnataka" -> listOf(
                DestinationCity(42, "Bengaluru", "Karnataka", "India", 1201),
                DestinationCity(43, "Mysuru", "Karnataka", "India", 1202),
                DestinationCity(44, "Mangalore", "Karnataka", "India", 1203)
            )
            "Kerala" -> listOf(
                DestinationCity(45, "Kochi", "Kerala", "India", 1301),
                DestinationCity(46, "Thiruvananthapuram", "Kerala", "India", 1302),
                DestinationCity(47, "Kozhikode", "Kerala", "India", 1303)
            )
            "Madhya Pradesh" -> listOf(
                DestinationCity(6, "Bhopal", "Madhya Pradesh", "India", 201),
                DestinationCity(7, "Indore", "Madhya Pradesh", "India", 202),
                DestinationCity(8, "Gwalior", "Madhya Pradesh", "India", 203),
                DestinationCity(9, "Jabalpur", "Madhya Pradesh", "India", 204),
                DestinationCity(10, "Ujjain", "Madhya Pradesh", "India", 205)
            )
            "Maharashtra" -> listOf(
                DestinationCity(48, "Mumbai", "Maharashtra", "India", 1401),
                DestinationCity(49, "Pune", "Maharashtra", "India", 1402),
                DestinationCity(50, "Nagpur", "Maharashtra", "India", 1403)
            )
            "Manipur" -> listOf(
                DestinationCity(51, "Imphal", "Manipur", "India", 1501),
                DestinationCity(52, "Bishnupur", "Manipur", "India", 1502),
                DestinationCity(53, "Thoubal", "Manipur", "India", 1503)
            )
            "Meghalaya" -> listOf(
                DestinationCity(54, "Shillong", "Meghalaya", "India", 1601),
                DestinationCity(55, "Cherrapunji", "Meghalaya", "India", 1602),
                DestinationCity(56, "Tura", "Meghalaya", "India", 1603)
            )
            "Mizoram" -> listOf(
                DestinationCity(57, "Aizawl", "Mizoram", "India", 1701),
                DestinationCity(58, "Lunglei", "Mizoram", "India", 1702),
                DestinationCity(59, "Champhai", "Mizoram", "India", 1703)
            )
            "Nagaland" -> listOf(
                DestinationCity(60, "Kohima", "Nagaland", "India", 1801),
                DestinationCity(61, "Dimapur", "Nagaland", "India", 1802),
                DestinationCity(62, "Mokokchung", "Nagaland", "India", 1803)
            )
            "Odisha" -> listOf(
                DestinationCity(63, "Bhubaneswar", "Odisha", "India", 1901),
                DestinationCity(64, "Puri", "Odisha", "India", 1902),
                DestinationCity(65, "Cuttack", "Odisha", "India", 1903)
            )
            "Punjab" -> listOf(
                DestinationCity(66, "Amritsar", "Punjab", "India", 2001),
                DestinationCity(67, "Ludhiana", "Punjab", "India", 2002),
                DestinationCity(68, "Jalandhar", "Punjab", "India", 2003)
            )
            "Rajasthan" -> listOf(
                DestinationCity(69, "Jaipur", "Rajasthan", "India", 2101),
                DestinationCity(70, "Udaipur", "Rajasthan", "India", 2102),
                DestinationCity(71, "Jodhpur", "Rajasthan", "India", 2103)
            )
            "Sikkim" -> listOf(
                DestinationCity(72, "Gangtok", "Sikkim", "India", 2201),
                DestinationCity(73, "Pelling", "Sikkim", "India", 2202),
                DestinationCity(74, "Namchi", "Sikkim", "India", 2203)
            )
            "Tamil Nadu" -> listOf(
                DestinationCity(75, "Chennai", "Tamil Nadu", "India", 2301),
                DestinationCity(76, "Madurai", "Tamil Nadu", "India", 2302),
                DestinationCity(77, "Coimbatore", "Tamil Nadu", "India", 2303)
            )
            "Telangana" -> listOf(
                DestinationCity(78, "Hyderabad", "Telangana", "India", 2401),
                DestinationCity(79, "Warangal", "Telangana", "India", 2402),
                DestinationCity(80, "Nizamabad", "Telangana", "India", 2403)
            )
            "Tripura" -> listOf(
                DestinationCity(81, "Agartala", "Tripura", "India", 2501),
                DestinationCity(82, "Udaipur", "Tripura", "India", 2502),
                DestinationCity(83, "Dharmanagar", "Tripura", "India", 2503)
            )
            "Uttar Pradesh" -> listOf(
                DestinationCity(84, "Agra", "Uttar Pradesh", "India", 2601),
                DestinationCity(85, "Varanasi", "Uttar Pradesh", "India", 2602),
                DestinationCity(86, "Lucknow", "Uttar Pradesh", "India", 2603)
            )
            "Uttarakhand" -> listOf(
                DestinationCity(87, "Dehradun", "Uttarakhand", "India", 2701),
                DestinationCity(88, "Haridwar", "Uttarakhand", "India", 2702),
                DestinationCity(89, "Nainital", "Uttarakhand", "India", 2703)
            )
            "West Bengal" -> listOf(
                DestinationCity(90, "Kolkata", "West Bengal", "India", 2801),
                DestinationCity(91, "Darjeeling", "West Bengal", "India", 2802),
                DestinationCity(92, "Siliguri", "West Bengal", "India", 2803)
            )
            else -> emptyList()
        }
    }
    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Top Tourist Cities in $stateName", fontSize = 22.sp, modifier = Modifier.padding(bottom = 16.dp))
        if (topCities.isEmpty()) {
            Text("No cities found for $stateName.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(topCities) { city ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    isLoading = true
                                    error = null
                                    try {
                                        val encodedCity = URLEncoder.encode(city.city, "UTF-8")
                                        val encodedState = URLEncoder.encode(city.state, "UTF-8")
                                        val url = "$BASE_URL/api/city-details?city=$encodedCity&state=$encodedState"
                                        val response: HttpResponse = httpClient.get(url)
                                        val raw = response.bodyAsText()
                                        println("Raw response: $raw")
                                        val result = response.body<DestinationCity>()
                                        onCitySelected(result.id.toString(), result.city)
                                    } catch (e: Exception) {
                                        error = "City not found or network error: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                            .padding(8.dp)
                    ) {
                        Text(city.city, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}