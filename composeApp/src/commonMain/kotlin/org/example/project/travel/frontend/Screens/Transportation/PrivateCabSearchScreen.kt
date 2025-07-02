package org.example.project.travel.frontEnd.Screens.Transportation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.example.project.travel.frontEnd.navigation.RootComponent
import org.example.project.travel.frontEnd.navigation.Screen

interface PrivateCabSearchScreenComponent {
    fun navigateTo(screen: Screen)
    fun pop()
}

class PrivateCabSearchScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent
) : PrivateCabSearchScreenComponent, ComponentContext by componentContext {
    override fun navigateTo(screen: Screen) {
        rootComponent.navigateTo(screen)
    }

    override fun pop() {
        rootComponent.pop()
    }
}

@Composable
fun PrivateCabSearchScreen(
    component: PrivateCabSearchScreenComponent
) {
    Text("Private Cab Search Screen")
}