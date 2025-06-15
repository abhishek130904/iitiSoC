package org.example.project.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.example.travel.viewmodel.FlightViewModel
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent
import org.example.project.travel.frontend.App
import org.example.project.travel.frontend.Screens.HotelScreen
import org.example.project.travel.frontend.Screens.TripItineraryScreen
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.RootComponentImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    TripItineraryScreen()
                }
            }
        }
    }
}

//class MainActivity : PreComposeActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val componentContext = defaultComponentContext()
//        val flightViewModel = FlightViewModel() // Create FlightViewModel instance
//        val rootComponent: RootComponent = RootComponentImpl(
//            componentContext = componentContext,
//            flightViewModel = flightViewModel
//        )
//        setContent {
//            App(rootComponent = rootComponent) // Pass rootComponent
//        }
//    }
//}