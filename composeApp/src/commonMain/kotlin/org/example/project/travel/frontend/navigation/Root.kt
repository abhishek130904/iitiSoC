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
import org.example.project.travel.frontEnd.Screens.ProfileScreen
import org.example.project.travel.frontend.auth.getCurrentFirebaseUserUid
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import org.example.project.travel.frontEnd.AppSettings
import org.example.project.travel.frontEnd.Screens.TripConfirmationScreen
import org.example.project.travel.frontEnd.Screens.OfflineScreen
import org.example.project.travel.frontEnd.network.NetworkMonitor
import ui.HomeScreen
import ui.TravelCategory
import androidx.compose.ui.platform.LocalContext
import org.example.project.travel.frontEnd.Screens.StateScreen as StateScreenComposable
import org.example.project.travel.frontEnd.Screens.CategoryDetailsScreen
import org.example.project.travel.frontEnd.Screens.MyTripsScreen

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
                        onProfileClick = { component.navigateTo(Screen.ProfileScreen) },
                        onCategoryClick = { category ->
                            component.navigateTo(
                                Screen.CategoryDetails(
                                    categoryTitle = category.title,
                                    categoryDescription = category.description,
                                    destinations = category.popularDestinations
                                )
                            )
                        }
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
                        onMyTripsClick = {
                            val userId = getCurrentFirebaseUserUid()
                            if (userId != null) {
                                component.navigateTo(Screen.MyTrips(userId))
                            }
                        }
                    )
                    is RootComponent.Child.StateScreen -> StateScreenComposable(
                        instance.screen.stateName, 
                        onCitySelected = { cityId, cityName -> 
                            component.navigateTo(Screen.CityDetails(
                                cityId.toString(),
                                cityName.toString()
                            )) 
                        }
                    )
                    is RootComponent.Child.CategoryDetails -> CategoryDetailsScreen(
                        categoryTitle = instance.screen.categoryTitle,
                        categoryDescription = instance.screen.categoryDescription,
                        destinations = instance.screen.destinations,
                        onDestinationClick = { destination ->
                            // Navigate to CityDetails with cityName only (cityId = null)
                            component.navigateTo(Screen.CityDetails(
                                cityId = null.toString(),
                                cityName = destination
                            ))
                        },
                        onBackClick = { component.pop() }
                    )
                    is RootComponent.Child.MyTrips -> MyTripsScreen(userId = instance.screen.userId)
                }
            }
        }
    }
}