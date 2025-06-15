package org.example.project.travel.frontend.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.example.project.travel.frontend.Screens.HotelScreen
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen


@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation()
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.FlightSearch -> FlightSearchScreen(instance.component)
            is RootComponent.Child.FlightDetail -> FlightDetailScreen(instance.component)
            is RootComponent.Child.Hotel -> HotelScreenWrapper(instance.component)

//            is RootComponent.Child.BusSearch -> BusSearchScreen(child.instance.component)
//            is RootComponent.Child.TrainSearch -> TrainSearchScreen(child.instance.component)
//            is RootComponent.Child.PrivateCabSearch -> PrivateCabSearchScreen(child.instance.component)
        }
    }
}