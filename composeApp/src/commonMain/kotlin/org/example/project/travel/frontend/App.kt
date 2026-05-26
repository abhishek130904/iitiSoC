package org.example.project.travel.frontend

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import moe.tlaster.precompose.navigation.BackHandler
import org.example.project.travel.frontEnd.Screens.CategoryDetailsScreen
import org.example.project.travel.frontEnd.Screens.CityDetailsScreen
import org.example.project.travel.frontEnd.Screens.MyTripsScreen
import org.example.project.travel.frontEnd.Screens.OnboardingScreen
import org.example.project.travel.frontEnd.Screens.ProfileScreen
import org.example.project.travel.frontEnd.Screens.TripConfirmationScreen
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel
import org.example.project.travel.frontend.Screens.HotelForTrainScreen
import org.example.project.travel.frontend.Screens.HotelScreenWrapper
import org.example.project.travel.frontend.Screens.StandaloneHotelSearchScreen
import org.example.project.travel.frontend.Screens.SignInScreen
import org.example.project.travel.frontend.Screens.SignUpScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightDetailScreen
import org.example.project.travel.frontend.Screens.Transportation.FlightSearchScreen
import org.example.project.travel.frontend.Screens.Transportation.TrainDetailsScreen
import org.example.project.travel.frontend.Screens.Transportation.TrainDetailsScreenComponentImpl
import org.example.project.travel.frontend.Screens.Transportation.TrainSearchScreen
import org.example.project.travel.frontend.Screens.TripItineraryScreen
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontend.auth.getCurrentFirebaseUserUid
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.example.project.travel.frontend.screen.SearchCityScreen
import ui.HomeScreen
import org.example.project.travel.frontEnd.Screens.StateScreen as StateScreenComposable

@Composable
fun App(
    rootComponent: RootComponent,
    authService: AuthService,
    googleSignInManager: GoogleSignInManager
) {
    MaterialTheme {
        val childStack by rootComponent.childStack.subscribeAsState()
        var selectedCityForTrain by remember { mutableStateOf<String?>(null) }

        when (val child = childStack.active.instance) {
            is RootComponent.Child.Onboarding -> OnboardingScreen(
                // Fix #25: Never pop from root — navigate to Login instead
                onFinished = { rootComponent.replaceAll(Screen.Login) },
                onNavigateToSignIn = { rootComponent.navigateTo(Screen.Login) }
            )
            is RootComponent.Child.Login -> SignInScreen(
                authService = authService,
                googleSignInManager = googleSignInManager,
                onLoginSuccess = { rootComponent.navigateTo(Screen.HomeScreen) },
                onSignUpClick = { child.component.onSignUpClick() }
            )
            is RootComponent.Child.Signup -> SignUpScreen(
                authService = authService,
                googleSignInManager = googleSignInManager,
                onBack = { rootComponent.pop() },
                onSignUpSuccess = { rootComponent.navigateTo(Screen.HomeScreen) }
            )
            is RootComponent.Child.HomeScreen -> HomeScreen(
                onNavigateToCitySearch = { rootComponent.navigateTo(Screen.CitySearchScreen) },
                onProfileClick = { rootComponent.navigateTo(Screen.ProfileScreen) },
                onCategoryClick = { category ->
                    rootComponent.navigateTo(
                        Screen.CategoryDetails(
                            categoryTitle = category.title,
                            categoryDescription = category.description,
                            destinations = category.popularDestinations
                        )
                    )
                }
            )
            is RootComponent.Child.CitySearchScreen -> SearchCityScreen(child.component, CitySearchViewModel())
            is RootComponent.Child.CityDetails -> CityDetailsScreen(child.component)
            is RootComponent.Child.FlightSearch -> FlightSearchScreen(child.component)
            is RootComponent.Child.FlightDetail -> FlightDetailScreen(child.component)
            is RootComponent.Child.Hotel -> HotelScreenWrapper(child.component)
            is RootComponent.Child.TripItinerary -> TripItineraryScreen(child.component)
            is RootComponent.Child.ProfileScreen -> {
                val uid = getCurrentFirebaseUserUid()
                ProfileScreen(
                    uid = uid,
                    authService = authService,
                    onLogout = { rootComponent.replaceAll(Screen.Login) },
                    onHomeClick = { rootComponent.replaceAll(Screen.HomeScreen) },
                    onMyTripsClick = { userId ->
                        rootComponent.navigateTo(Screen.MyTrips(userId))
                    }
                )
            }
            is RootComponent.Child.TripConfirmation -> {
                BackHandler(enabled = true) { /* Block back on confirmation */ }
                TripConfirmationScreen(
                    context = LocalContext.current,
                    destination = child.screen.destination,
                    dates = child.screen.dates,
                    flightDetails = child.screen.flightDetails,
                    hotelDetails = child.screen.hotelDetails,
                    activities = child.screen.activities,
                    meals = child.screen.meals,
                    costBreakdown = child.screen.costBreakdown,
                    notes = child.screen.notes,
                    onHomeClick = { rootComponent.replaceAll(Screen.HomeScreen) },
                    onMyTripsClick = {
                        val userId = getCurrentFirebaseUserUid()
                        if (userId != null) {
                            rootComponent.navigateTo(Screen.MyTrips(userId))
                        }
                    }
                )
            }
            is RootComponent.Child.StateScreen -> StateScreenComposable(
                child.screen.stateName,
                onCitySelected = { cityId, cityName ->
                    rootComponent.navigateTo(Screen.CityDetails(cityId.toString(), cityName.toString()))
                }
            )
            is RootComponent.Child.CategoryDetails -> CategoryDetailsScreen(
                categoryTitle = child.screen.categoryTitle,
                categoryDescription = child.screen.categoryDescription,
                destinations = child.screen.destinations,
                onDestinationClick = { destination ->
                    rootComponent.navigateTo(Screen.CityDetails(cityId = null.toString(), cityName = destination))
                },
                onBackClick = { rootComponent.pop() }
            )
            is RootComponent.Child.MyTrips -> MyTripsScreen(
                userId = child.screen.userId,
                onHomeClick = { rootComponent.replaceAll(Screen.HomeScreen) }
            )
            is RootComponent.Child.TrainSearch -> TrainSearchScreen(child.component)
            is RootComponent.Child.TrainDetails -> TrainDetailsScreen(
                fromStation = child.fromStation,
                toStation = child.toStation,
                component = TrainDetailsScreenComponentImpl(rootComponent)
            )
            is RootComponent.Child.HotelForTrain -> HotelForTrainScreen(
                selectedTrain = child.selectedTrain,
                selectedCoach = child.selectedCoach,
                fare = child.fare,
                onNavigateBack = { rootComponent.pop() },
                onNavigateToNext = { selectedHotel ->
                    val cityName = selectedCityForTrain ?: child.selectedTrain.to_station_name
                    rootComponent.navigateTo(
                        Screen.TripItinerary(
                            selectedTrain = child.selectedTrain,
                            selectedHotel = selectedHotel,
                            selectedCityName = cityName,
                            selectedCoach = child.selectedCoach,
                            fare = child.fare
                        )
                    )
                },
                onCitySelected = { city -> selectedCityForTrain = city.city }
            )
            is RootComponent.Child.HotelSearch -> StandaloneHotelSearchScreen()
        }
    }
}