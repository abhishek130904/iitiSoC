package org.example.project.travel.frontend.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.example.project.travel.frontEnd.Screens.OnboardingScreen
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.SignInScreen
import org.example.project.travel.frontend.Screens.SignUpScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.screen.SearchCityScreen
import org.example.project.travel.frontEnd.Screens.CityDetailsScreen
import org.example.project.travel.frontend.Screens.TripItineraryScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.travel.frontEnd.Screens.ProfileScreen
import org.example.project.travel.frontend.auth.getCurrentFirebaseUserUid
import org.example.project.travel.frontend.auth.UserProfile
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.project.travel.frontend.auth.fetchUserProfile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import org.example.project.travel.frontEnd.AppSettings
import org.example.project.travel.frontEnd.Screens.TripConfirmationScreen
import org.example.project.travel.frontEnd.Screens.OfflineScreen
import org.example.project.travel.frontEnd.network.NetworkMonitor
import ui.HomeScreen
import androidx.compose.ui.platform.LocalContext

@Composable
fun RootContent(
    component: RootComponent,
    authService: AuthService,
    googleSignInManager: GoogleSignInManager
) {
    // Always show onboarding if not seen, even if user is logged in
    val hasSeenOnboarding = AppSettings.hasSeenOnboarding()
    val isLoggedIn = getCurrentFirebaseUserUid() != null
    println("OnboardingDebug: hasSeenOnboarding = $hasSeenOnboarding, isLoggedIn = $isLoggedIn")
    val startScreen = when {
        !hasSeenOnboarding -> Screen.Onboarding
        isLoggedIn -> Screen.HomeScreen
        else -> Screen.Onboarding
    }
    // Only set initial screen on first launch
    val didInit = remember { mutableStateOf(false) }
    if (!didInit.value) {
        component.replaceAll(startScreen)
        didInit.value = true
    }

    // Start network monitoring
    LaunchedEffect(Unit) { NetworkMonitor.start() }
    val isOnline = NetworkMonitor.isOnline.collectAsState().value

    MaterialTheme {
        if (!isOnline) {
            OfflineScreen(onRetry = { NetworkMonitor.start() })
        } else {
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
                        onNavigateToCitySearch = { component.navigateTo(Screen.CitySearchScreen) },
                        onProfileClick = { component.navigateTo(Screen.ProfileScreen) }
                    )
                    is RootComponent.Child.FlightSearch -> FlightSearchScreen(instance.component)
                    is RootComponent.Child.FlightDetail -> FlightDetailScreen(instance.component)
                    is RootComponent.Child.Hotel -> HotelScreenWrapper(instance.component)
                    is RootComponent.Child.CitySearchScreen -> SearchCityScreen(instance.component, CitySearchViewModel())
                    is RootComponent.Child.CityDetails -> CityDetailsScreen(instance.component)
                    is RootComponent.Child.TripItinerary -> TripItineraryScreen(instance.component)
                    is RootComponent.Child.ProfileScreen -> {
                        val uid = getCurrentFirebaseUserUid()
                        ProfileScreen(
                            uid = uid,
                            authService = authService,
                            password = "",
                            onLogout = { component.replaceAll(Screen.Login) },
                            onHomeClick = { component.replaceAll(Screen.HomeScreen) }
                        )
                    }
                    is RootComponent.Child.TripConfirmation -> TripConfirmationScreen(
                        context = LocalContext.current,
                        destination = instance.screen.destination,
                        dates = instance.screen.dates,
                        flightDetails = instance.screen.flightDetails,
                        hotelDetails = instance.screen.hotelDetails,
                        activities = instance.screen.activities,
                        meals = instance.screen.meals,
                        costBreakdown = instance.screen.costBreakdown,
                        notes = instance.screen.notes,
                        onHomeClick = { component.replaceAll(Screen.HomeScreen) },
                        onMyTripsClick = { /* TODO: Navigate to My Trips screen when implemented */ }
                    )
                }
            }
        }
    }
}