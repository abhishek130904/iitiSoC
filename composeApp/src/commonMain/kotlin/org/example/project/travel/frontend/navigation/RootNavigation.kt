package org.example.project.travel.frontend.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.travel.model.dto.FlightDTO
import com.example.travel.viewmodel.FlightViewModel
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.HotelScreenComponent
import org.example.project.travel.frontend.Screens.HotelScreenComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreenComponent
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreenComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreenComponent
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreenComponentImpl

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>
    val flightViewModel: FlightViewModel

    fun navigateTo(screen: Screen)
    fun pop()

    sealed class Child {
        data class FlightSearch(val component: FlightSearchScreenComponent) : Child()
        data class FlightDetail(val component: FlightDetailScreenComponent) : Child()
        data class Hotel(val component: HotelScreenComponent) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext,
    override val flightViewModel: FlightViewModel
    ) : RootComponent, ComponentContext by componentContext {
        private val navigation = StackNavigation<Screen>()
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Screen.serializer(),
        initialConfiguration = Screen.FlightSearch,
        handleBackButton = true,
        childFactory = ::createChild
    )

    init {
        println("RootComponentImpl: Initialized with initial screen - ${childStack.value.active.configuration}")
    }

    override fun navigateTo(screen: Screen) {
        println("RootComponentImpl: Navigating to $screen")
        navigation.push(screen)
        println("RootComponentImpl: Navigation stack after push - active screen=${childStack.value.active.configuration}")
    }

    override fun pop() {
        println("RootComponentImpl: Popping from stack")
        navigation.pop()
        println("RootComponentImpl: Navigation stack after pop - active screen=${childStack.value.active.configuration}")
    }

    private fun createChild(screen: Screen, componentContext: ComponentContext): RootComponent.Child {
        println("RootComponentImpl: Creating child for screen=$screen")
        return when (screen) {
            Screen.FlightSearch -> {
                val component = FlightSearchScreenComponentImpl(componentContext, this)
                println("RootComponentImpl: Created FlightSearchScreenComponent")
                RootComponent.Child.FlightSearch(component)
            }
            is Screen.FlightDetail -> {
                val component = FlightDetailScreenComponentImpl(componentContext, this, screen.flights)
                println("RootComponentImpl: Created FlightDetailScreenComponent - flights=${screen.flights}")
                RootComponent.Child.FlightDetail(component)
            }
            is Screen.Hotel -> {
                val component = HotelScreenComponentImpl(componentContext, this, screen.flightPrice, screen.flightCurrency)
                println("RootComponentImpl: Created HotelScreenComponent - flightPrice=${screen.flightPrice}, flightCurrency=${screen.flightCurrency}")
                RootComponent.Child.Hotel(component)
            }
        }
    }
}