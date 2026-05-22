package org.example.project.travel.frontend.config

import org.example.project.travel.BuildConfig

actual object ApiConfig {
    actual val unsplashApiKey: String = BuildConfig.UNSPLASH_KEY
    actual val recommendationBaseUrl: String = BuildConfig.RECOMMENDATION_BASE_URL
}
