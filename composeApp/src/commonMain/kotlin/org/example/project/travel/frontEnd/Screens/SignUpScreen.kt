package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.travel.frontEnd.auth.AuthService
import kotlinx.coroutines.launch        // Already present
import org.example.project.travel.frontEnd.auth.GoogleSignInManager
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.gg
import travelfrontend.composeapp.generated.resources.login_background
import org.example.project.travel.frontEnd.auth.getCurrentFirebaseUserUid
import com.google.firebase.auth.FirebaseAuth

// Custom blue color
val AppBlue = Color(red = 23, green = 111, blue = 243)

@Composable
fun SignUpScreen(
    authService: AuthService,
    googleSignInManager: GoogleSignInManager,
    onBack: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSigningUp by remember { mutableStateOf(false) }
    var isSigningUpWithGoogle by remember { mutableStateOf(false) }
    var showGoogleProfileDialog by remember { mutableStateOf(false) }
    var googleUid by remember { mutableStateOf("") }
    var googleName by remember { mutableStateOf("") }
    var googleAge by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        val painter = painterResource(Res.drawable.login_background)
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Sign Up", fontSize = 32.sp, color = Color.White)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it},
                label = { Text("Name")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppBlue,
                    cursorColor = AppBlue
                )
            )
            OutlinedTextField(
                value = age,
                onValueChange = { age = it},
                label = { Text("Age") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppBlue,
                    cursorColor = AppBlue
                )
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBlue,
                    cursorColor = AppBlue
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBlue,
                    cursorColor = AppBlue
                )
            )

            errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        isSigningUp = true
                        val userAge = age.toIntOrNull()
                        if (userAge == null || name.isBlank()) {
                            errorMessage = "Please enter a valid name and age."
                            isSigningUp = false
                            return@launch
                        }
                        val result = authService.createUserWithEmailAndPassword(email.trim(), password.trim())
                        result.onSuccess { uid ->
                            authService.saveUserProfile(uid, name.trim(), userAge, email.trim())
                            isSigningUp = false
                            onSignUpSuccess()
                        }.onFailure {
                            errorMessage = "Error: ${it.message}"
                            isSigningUp = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
                enabled = !isSigningUp
            ) {
                Text(if (isSigningUp) "Signing Up..." else "Sign Up", color = Color.White)
            }

            Text(
                text = "OR SIGN UP WITH",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Image(
                painter = painterResource(Res.drawable.gg),
                contentDescription = "Google Sign-In",
                modifier = Modifier
                    .size(40.dp) // Adjust size as needed
                    .clickable(enabled = !isSigningUpWithGoogle) {
                        coroutineScope.launch {
                            isSigningUpWithGoogle = true
                            googleSignInManager.signOut()
                            googleSignInManager.signInWithGoogle().onSuccess {
                                errorMessage = null
                                val uid = getCurrentFirebaseUserUid()
                                if (uid != null) {
                                    googleUid = uid
                                    showGoogleProfileDialog = true
                                } else {
                                    errorMessage = "Google sign-in failed: No user found."
                                }
                            }.onFailure { e ->
                                errorMessage = "Google Sign-Up Failed: ${e.message}"
                            }
                            isSigningUpWithGoogle = false
                        }
                    }
            )

            if (showGoogleProfileDialog) {
                AlertDialog(
                    onDismissRequest = { showGoogleProfileDialog = false },
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    textContentColor = Color.Black,
                    title = { Text("Complete Profile") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = googleName,
                                onValueChange = { googleName = it },
                                label = { Text("Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = googleAge,
                                onValueChange = { googleAge = it },
                                label = { Text("Age") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val userAge = googleAge.toIntOrNull()
                            if (googleName.isBlank() || userAge == null) {
                                errorMessage = "Please enter a valid name and age."
                                return@Button
                            }
                            coroutineScope.launch {
                                val user = FirebaseAuth.getInstance().currentUser
                                val email = user?.email
                                authService.saveUserProfile(googleUid, googleName.trim(), userAge, email)
                                showGoogleProfileDialog = false
                                onSignUpSuccess()
                            }
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBlue,
                                contentColor = Color.White
                            )) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showGoogleProfileDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBlue
                            )) {
                            Text("Cancel")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Back to Sign In",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }
        }
    }
}