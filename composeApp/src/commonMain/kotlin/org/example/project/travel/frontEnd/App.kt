package org.example.project.travel.frontend

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.project.travel.frontEnd.Screens.OnboardingScreen
import org.example.project.travel.frontend.Screens.SignInScreen
import org.example.project.travel.frontend.Screens.SignUpScreen
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen

@Composable
fun App(
    rootComponent: RootComponent,
    authService: AuthService,
    googleSignInManager: GoogleSignInManager
) {
    MaterialTheme {
        val childStack by rootComponent.childStack.subscribeAsState()
        when (val child = childStack.active.instance) {
            is RootComponent.Child.Onboarding -> OnboardingScreen(
                onFinished = { rootComponent.pop() }, // Pop back if needed (e.g., skip)
                onNavigateToSignIn = { rootComponent.navigateTo(Screen.Login) }
            )
            is RootComponent.Child.Login -> SignInScreen(
                authService = authService,
                googleSignInManager = googleSignInManager,
                onLoginSuccess = { child.component.onLoginSuccess() },
                onSignUpClick = { child.component.onSignUpClick() }
            )
            is RootComponent.Child.Signup -> SignUpScreen(
                authService = authService,
                googleSignInManager = googleSignInManager,
                onBack = { rootComponent.navigateTo(Screen.Login) },
                onSignUpSuccess = { rootComponent.navigateTo(Screen.FlightSearch) }
            )
            is RootComponent.Child.FlightSearch -> FlightSearchScreen(child.component)
            is RootComponent.Child.FlightDetail -> FlightDetailScreen(child.component)
            is RootComponent.Child.Hotel -> HotelScreenWrapper(child.component)
            is RootComponent.Child.TripItinerary -> org.example.project.travel.frontend.Screens.TripItineraryScreen(child.component)
            else -> throw IllegalStateException("Unhandled navigation child: $child")
        }
    }
}