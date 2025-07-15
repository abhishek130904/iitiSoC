package org.example.project.travel.frontend.auth

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class GoogleSignInManager(private val context: ComponentActivity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var launcher: ActivityResultLauncher<Intent>

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("690975274529-9maki9jfqqpbeh81d4ho96k9crnrs5hh.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        launcher = context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            println("Activity result: $result")
            println("Intent data: ${result.data?.extras}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                println("Google account: $account")
                callback?.invoke(Result.success(account.idToken!!))
            } catch (e: ApiException) {
                println("ApiException:  ${e.statusCode}, ${e.message}")
                e.printStackTrace()
                if (e.statusCode == 12501) { // SIGN_IN_CANCELLED
                    // User cancelled sign-in (pressed back), handle gracefully
                    callback?.invoke(Result.failure(Exception("Sign-in cancelled by user")))
                } else {
                    callback?.invoke(Result.failure(e))
                }
            } finally {
                callback = null
            }
        }
    }

    private var callback: ((Result<String>) -> Unit)? = null

    actual suspend fun signInWithGoogle(): Result<Unit> {
        signOut()
        val idToken = suspendCancellableCoroutine { continuation ->
            callback = { result ->
                result.onSuccess { token ->
                    continuation.resume(token)
                }.onFailure { e ->
                    continuation.resumeWithException(e)
                }
            }
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    actual suspend fun signOut() {
        googleSignInClient.signOut().await()
        auth.signOut()
    }
}
