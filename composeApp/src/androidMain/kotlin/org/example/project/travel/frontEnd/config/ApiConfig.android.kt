package org.example.project.travel.frontend.config

import com.tripbuddy.app.BuildConfig

actual object ApiConfig {
    actual val unsplashApiKey: String = BuildConfig.UNSPLASH_KEY
    actual val recommendationBaseUrl: String = BuildConfig.RECOMMENDATION_BASE_URL
}
