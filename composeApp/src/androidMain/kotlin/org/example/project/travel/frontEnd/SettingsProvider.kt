package org.example.project.travel.frontend.ui

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

private var appContext: Context? = null

fun initSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun getSettings(): Settings {
    val context = appContext ?: error("Settings context not initialized. Call initSettings(context) first.")
    val sharedPrefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
} 