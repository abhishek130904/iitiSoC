package org.example.project.travel.frontEnd

import com.russhwolf.settings.Settings

object AppSettings {
    private val settings: Settings = getSettings()

    fun hasSeenOnboarding(): Boolean = settings.getBoolean("hasSeenOnboarding", false)
    fun setHasSeenOnboarding() = settings.putBoolean("hasSeenOnboarding", true)
} 