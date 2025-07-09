package org.example.project.travel.frontend.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.firestore
import org.example.project.travel.frontend.auth.UserProfile

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
    actual suspend fun saveUserProfile(uid: String, name: String, age: Int, email: String?) {
        val db = Firebase.firestore
        val userMap = mutableMapOf<String, Any>(
            "name" to name,
            "age" to age
        )
        if (email != null) userMap["email"] = email
        db.collection("users").document(uid).set(userMap).await()
    }
    actual suspend fun signOut() {
        auth.signOut()
    }
}

actual suspend fun fetchUserProfile(uid: String): UserProfile? {
    val db = Firebase.firestore
    val doc = db.collection("users").document(uid).get().await()
    val name = doc.getString("name") ?: return null
    val age = doc.getLong("age")?.toInt() ?: return null
    val email = doc.getString("email") ?: ""
    return UserProfile(email = email, name = name, age = age)
}

actual fun getCurrentFirebaseUserUid(): String? {
    return com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
}