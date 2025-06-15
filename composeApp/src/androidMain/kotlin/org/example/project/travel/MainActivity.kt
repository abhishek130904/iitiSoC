package org.example.project.travel

import TravelCategoryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.travel.frontEnd.Screens.DestiSelect
import org.example.project.travel.frontEnd.Screens.TopActivity


// You can also import other screens like:
// import org.example.project.travel.frontEnd.Screens.TopActivity
// import org.example.project.travel.frontEnd.Screens.TravelCategoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DestiSelect()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}
