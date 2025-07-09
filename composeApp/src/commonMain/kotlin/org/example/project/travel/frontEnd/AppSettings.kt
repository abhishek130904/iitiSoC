package org.example.project.travel.frontEnd

import com.russhwolf.settings.Settings
import org.example.project.travel.frontEnd.ui.getSettings

object AppSettings {
    private val settings: Settings = getSettings()

    fun hasSeenOnboarding(): Boolean = settings.getBoolean("hasSeenOnboarding", false)
    fun setHasSeenOnboarding() = settings.putBoolean("hasSeenOnboarding", true)
}