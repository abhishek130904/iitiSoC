package org.example.project.travel.frontend.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

actual class AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    actual suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "Unknown UID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "Unknown UID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}