package org.example.project.travel.frontend.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.travel.viewmodel.FlightViewModel
import org.example.project.travel.frontend.Screens.HotelScreenComponent
import org.example.project.travel.frontend.Screens.HotelScreenComponentImpl
import org.example.project.travel.frontend.Screens.SignInScreenComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreenComponent
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreenComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreenComponent
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreenComponentImpl
import org.example.project.travel.frontend.auth.AuthService
import ui.HomeScreenComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>
    val flightViewModel: FlightViewModel

    fun navigateTo(screen: Screen)
    fun pop()

    sealed class Child {
        data class Onboarding(val component: Any) : Child()
        data class HomeScreen(val component: Any) : Child()
        data class Login(val component: SignInScreenComponentImpl) : Child() // Changed to LoginScreenComponent
        data class Signup(val component: Any) : Child()
        data class FlightSearch(val component: FlightSearchScreenComponent) : Child()
        data class FlightDetail(val component: FlightDetailScreenComponent) : Child()
        data class Hotel(val component: HotelScreenComponent) : Child()
    }
}

fun createRootComponent(
    componentContext: ComponentContext,
    flightViewModel: FlightViewModel,
    authService: AuthService
): RootComponent {
    return RootComponentImpl(componentContext, flightViewModel, authService)
}

class RootComponentImpl(
    componentContext: ComponentContext,
    override val flightViewModel: FlightViewModel,
    private val authService: AuthService // Add AuthService
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Screen>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Screen.serializer(),
        initialConfiguration = Screen.Onboarding, // Starts at Login screen
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
            Screen.Login -> {
                val component = SignInScreenComponentImpl( // Use SignInScreenComponent for Login
                    componentContext = componentContext,
                    rootComponent = this,
                    authService = authService
                )
                println("RootComponentImpl: Created SignInScreenComponent")
                RootComponent.Child.Login(component)
            }
            Screen.Signup -> {
                println("RootComponentImpl: Created Signup placeholder component")
                RootComponent.Child.Signup(Any())
            }
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
            Screen.Onboarding -> {
                println("RootComponentImpl: Created Onboarding placeholder component")
                RootComponent.Child.Onboarding(Any())
            }

            Screen.HomeScreen ->{
                RootComponent.Child.HomeScreen(Any())
            }

        }
    }
}