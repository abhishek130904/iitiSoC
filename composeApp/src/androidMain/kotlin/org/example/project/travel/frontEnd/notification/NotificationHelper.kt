package org.example.project.travel.frontEnd.notification

import org.example.project.travel.MainActivity

actual fun showTestNotification(context: Any) {
    val activity = context as? MainActivity
    activity?.showTripNotification(
        "Trip Confirmed!",
        "Your trip has been successfully confirmed. Check the app for details."
    )
} 