package org.example.project.travel.frontend

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.travel.viewmodel.FlightViewModel
import moe.tlaster.precompose.viewmodel.viewModel
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.RootComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen


@Composable
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        val childStack by rootComponent.childStack.subscribeAsState()
        when (val child = childStack.active.instance) {
            is RootComponent.Child.FlightSearch -> FlightSearchScreen(child.component)
            is RootComponent.Child.FlightDetail -> FlightDetailScreen(child.component)
            is RootComponent.Child.Hotel -> HotelScreenWrapper(child.component)
        }
    }
}