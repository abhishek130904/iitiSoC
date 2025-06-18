package org.example.project.travel.frontend.auth

actual class GoogleSignInManager {
    actual suspend fun signInWithGoogle(): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Google Sign-In is not supported on iOS yet"))
    }

    actual suspend fun signOut() {
        throw UnsupportedOperationException("Google Sign-In is not supported on iOS yet")
    }
}