package org.example.project.travel

import TravelCategoryScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.travel.viewmodel.FlightViewModel
import com.google.firebase.FirebaseApp
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent
import org.example.project.travel.frontend.Screens.TopActivity
import org.example.project.travel.frontend.App
import org.example.project.travel.frontend.Screens.HotelScreen
import org.example.project.travel.frontend.Screens.TripItineraryScreen
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.RootComponentImpl
import org.example.project.travel.frontend.navigation.RootContent
import org.example.project.travel.frontend.navigation.Screen
import org.example.project.travel.frontend.navigation.createRootComponent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travel.network.ApiClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.travel.frontend.model.DestinationCity


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Initialize Firebase before creating AuthService
//        FirebaseApp.initializeApp(this)
//
//        val flightViewModel = FlightViewModel()
//        val authService = AuthService()
//        val googleSignInManager = GoogleSignInManager(this)
//        val root = createRootComponent(
//            componentContext = defaultComponentContext(),
//            flightViewModel = flightViewModel,
//            authService = authService,
//
//        )
//
//        setContent {
//            RootContent(
//                 component = root,
//                authService = authService,
//                googleSignInManager = googleSignInManager
//            )
//        }
//    }
//}
//
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase before creating AuthService
        FirebaseApp.initializeApp(this)

        // Setup Decompose lifecycle manually
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)

        val flightViewModel = FlightViewModel()
        val authService = AuthService()
        val googleSignInManager = GoogleSignInManager(this)
        val root = createRootComponent(
            componentContext = defaultComponentContext(),
            flightViewModel = flightViewModel,
            authService = authService,
        )

        setContent {
            RootContent(
                component = root,
                authService = authService,
                googleSignInManager = googleSignInManager
            )
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            val viewModel = viewModel<CitySearchViewModel<DestinationCity>>(
//                factory = object : ViewModelProvider.Factory {
//                    @Suppress("UNCHECKED_CAST")
//                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                        return CitySearchViewModel<DestinationCity>() as T
//                    }
//                }
//            )
//            DestinationCityScreen(viewModel = viewModel)
//        }
//    }
//}
