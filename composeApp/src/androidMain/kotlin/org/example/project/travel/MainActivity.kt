package org.example.project.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.travel.frontEnd.Screens.TripConfirmationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripConfirmationScreen()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    TripConfirmationScreen()
}