package org.example.project.travel.frontend.Screens

import androidx.compose.runtime.Composable
import com.example.travel.model.dto.FlightDTO
import org.example.project.travel.frontend.navigation.Screen


@Composable
fun HotelScreenWrapper(
    component: HotelScreenComponent
) {
    HotelScreen(
        flightPrice = component.flightPrice,
        flightCurrency = component.flightCurrency,
        onNavigateBack = {
            component.navigateTo(Screen.FlightDetail(flights = emptyList<FlightDTO>()))
        }
    )
}