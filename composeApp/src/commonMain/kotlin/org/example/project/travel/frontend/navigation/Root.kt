package org.example.project.travel.frontend.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.example.project.travel.frontEnd.Screens.OnboardingScreen
import org.example.project.travel.frontend.Screens.HotelScreen
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.SignInScreen
import org.example.project.travel.frontend.Screens.SignUpScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontend.model.DestinationCity
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.screen.SearchCityScreen
import ui.HomeScreen
import org.example.project.travel.frontEnd.Screens.CityDetailsScreen



@Composable
fun RootContent(
    component: RootComponent,
    authService: AuthService,
    googleSignInManager: GoogleSignInManager
) {
    MaterialTheme {
        Children(
            stack = component.childStack,
            animation = stackAnimation(fade()) // Using fade animation for simplicity
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.Onboarding -> OnboardingScreen(
                    onFinished = { component.pop()},
                    onNavigateToSignIn = { component.navigateTo(Screen.Login) }
                )
                is RootComponent.Child.Login -> SignInScreen(
                    authService = authService,
                    googleSignInManager = googleSignInManager,
                    onLoginSuccess = { component.navigateTo(Screen.HomeScreen) },
                    onSignUpClick = instance.component.onSignUpClick
                )
                is RootComponent.Child.Signup -> SignUpScreen(
                    authService = authService,
                    googleSignInManager = googleSignInManager,
                    onBack = { component.pop() },
                    onSignUpSuccess = { component.navigateTo(Screen.HomeScreen) }
                )
                is RootComponent.Child.HomeScreen -> HomeScreen(
                    userName = "TRAVELER", // Pass userName from component
                    onNavigateToCitySearch = { component.navigateTo(Screen.CitySearchScreen) }
                )
                is RootComponent.Child.FlightSearch -> FlightSearchScreen(instance.component)
                is RootComponent.Child.FlightDetail -> FlightDetailScreen(instance.component)
                is RootComponent.Child.Hotel -> HotelScreenWrapper(instance.component)
                is RootComponent.Child.CitySearchScreen -> SearchCityScreen(instance.component, CitySearchViewModel())
                is RootComponent.Child.CityDetails -> CityDetailsScreen(instance.component)
            }
        }
    }
}