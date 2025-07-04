package org.example.project.travel.frontend.auth

expect class GoogleSignInManager {
    suspend fun signInWithGoogle(): Result<Unit>
    suspend fun signOut()
}

data class GoogleUser(val uid : String, val name: String)