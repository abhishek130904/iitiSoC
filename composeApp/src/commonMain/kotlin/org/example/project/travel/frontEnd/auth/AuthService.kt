package org.example.project.travel.frontend.auth

expect class AuthService {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String>
}