package org.example.project.travel.frontEnd.Screens.Transportation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.example.project.travel.frontEnd.navigation.RootComponent
import org.example.project.travel.frontEnd.navigation.Screen


interface TrainSearchScreenComponent {
    fun navigateTo(screen: Screen)
    fun pop()
}

class TrainSearchScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : TrainSearchScreenComponent, ComponentContext by componentContext {
    override fun navigateTo(screen: Screen) {
        rootComponent.navigateTo(screen)
    }

    override fun pop() {
        rootComponent.pop()
    }
}

@Composable
fun TrainSearchScreen(
    component: TrainSearchScreenComponent
) {
    Text("Train Search Screen")
}