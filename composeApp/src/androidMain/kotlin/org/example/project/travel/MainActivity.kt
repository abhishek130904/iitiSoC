package org.example.project.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.travel.frontEnd.Screens.LoginPage
import ui.TravelAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelAppScreen("shrawani")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    LoginPage()
}