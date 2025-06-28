//package org.example.project.travel.frontEnd.Screens
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.tooling.preview.Preview
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
//import org.example.project.travel.frontend.model.DestinationCity
//import org.example.project.travel.frontend.screen.CitySearchScreenComponent
//import org.example.project.travel.frontend.screen.SearchCityScreen
//
//@Preview(showBackground = true, name = "SearchCityScreenPreview")
//@Composable
//fun SearchCityScreenPreview() {
//    // Mock CitySearchViewModel
//    val mockCities = listOf(
//        DestinationCity(id = 1, city = "Goa", state = "Goa", country = "India", cityCode = 101),
//        DestinationCity(id = 2, city = "Jaipur", state = "Rajasthan", country = "India", cityCode = 102)
//    )
//    val mockViewModel = object : CitySearchViewModel<DestinationCity>() {
//        override val cities: StateFlow<List<DestinationCity>> = MutableStateFlow(mockCities)
//        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)
//        override val error: StateFlow<String?> = MutableStateFlow(null)
//    }
//
//    // Mock CitySearchScreenComponentImpl
//    val mockComponent = object : CitySearchScreenComponent {
//        override fun onCitySelected(city: DestinationCity) {
//            println("Selected city for preview: ${city.city}")
//        }
//        override fun onBack() {
//            println("Back pressed in preview")
//        }
//    }
//
//    // Render the screen with mock data
//    SearchCityScreen(component = mockComponent, viewModel = mockViewModel)
//}