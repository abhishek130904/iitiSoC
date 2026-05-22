package org.example.project.travel.frontend.config

/**
 * Platform-specific API configuration.
 * Secrets are injected via BuildConfig (Android) or environment variables (other platforms).
 *
 * IMPORTANT: Never hardcode API keys here. Add them to local.properties:
 *   unsplash.api.key=YOUR_KEY_HERE
 *   recommendation.base.url=https://your-production-url.com
 */
expect object ApiConfig {
    /** Unsplash API client_id — loaded from BuildConfig on Android */
    val unsplashApiKey: String

    /** Recommendation service base URL — loaded from BuildConfig on Android */
    val recommendationBaseUrl: String
}
