package org.example.project.travel.frontEnd.auth

expect class AuthService {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun saveUserProfile(uid: String, name: String, age: Int, email: String?): Unit
}

expect fun getCurrentFirebaseUserUid(): String?

data class UserProfile(
    val email: String,
    val name: String,
    val age: Int
)

expect suspend fun fetchUserProfile(uid: String): UserProfile?