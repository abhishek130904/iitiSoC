package org.example.project.travel

import android.annotation.SuppressLint
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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.AlarmManager
import org.example.project.travel.R
import org.example.project.travel.frontend.ui.initSettings
import android.util.Log


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

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        // Initialize Firebase before creating AuthService
        FirebaseApp.initializeApp(this)

        // Initialize multiplatform settings
        initSettings(this)

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

        // Initialize network monitor with application context
        org.example.project.travel.frontEnd.network.ui.NetworkMonitor.init(applicationContext)

        // --- Local Notification Channel Setup ---
        createNotificationChannel()
        // Example: Show notification immediately (uncomment to test)
        // showTripNotification("Trip Reminder", "Your trip to Goa starts tomorrow!")
        // Example: Schedule notification for 10 seconds later (uncomment to test)
        // scheduleTripNotification("Trip Reminder", "Your trip to Goa starts tomorrow!", System.currentTimeMillis() + 10000)
        // --- End Notification Setup ---

        setContent {
            RootContent(
                component = root,
                authService = authService,
                googleSignInManager = googleSignInManager
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "trip_channel_id",
                "Trip Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission", "NotificationPermission")
    fun showTripNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, "trip_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Restore to app icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    fun scheduleTripNotification(title: String, message: String, triggerAtMillis: Long) {
        val intent = Intent(this, TripReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
}

// --- BroadcastReceiver for scheduled notifications ---
class TripReminderReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission", "NotificationPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Trip Reminder"
        val message = intent.getStringExtra("message") ?: "You have a trip event!"
        val builder = NotificationCompat.Builder(context, "trip_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
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
