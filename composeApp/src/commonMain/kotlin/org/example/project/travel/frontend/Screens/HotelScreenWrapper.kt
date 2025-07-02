package org.example.project.travel.frontEnd.Screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.travel.model.dto.FlightDTO
import org.example.project.travel.frontEnd.navigation.Screen


@Composable
fun HotelScreenWrapper(
    component: HotelScreenComponent
) {
    var selectedCity by remember { mutableStateOf<org.example.project.travel.frontEnd.model.DestinationCity?>(null) }
    HotelScreen(
        selectedFlight = component.selectedFlight,
        onNavigateBack = {
            component.goBack()
        },
        onNavigateToNext = { selectedHotel ->
            if (selectedCity != null) {
                component.navigateTo(
                    Screen.TripItinerary(
                        selectedFlight = component.selectedFlight,
                        selectedHotel = selectedHotel,
                        selectedCityName = selectedCity!!.city
                    )
                )
            }
        },
        onCitySelected = { city -> selectedCity = city }
    )
}