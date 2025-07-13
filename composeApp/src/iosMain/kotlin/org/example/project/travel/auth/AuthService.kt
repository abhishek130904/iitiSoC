package org.example.project.travel.frontend.auth

actual class AuthService {
    actual suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        throw NotImplementedError("Firebase Authentication not implemented for iOS yet.")
    }

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String> {
        throw NotImplementedError("Firebase Authentication not implemented for iOS yet.")
    }

    actual suspend fun doesUserExist(email: String): Boolean {
        // Not implemented for iOS
        return false
    }
}