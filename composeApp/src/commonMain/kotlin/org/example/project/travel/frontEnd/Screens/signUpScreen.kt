package org.example.project.travel.frontend.Screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import kotlinx.coroutines.CoroutineScope  // Added explicitly
import kotlinx.coroutines.launch        // Already present
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.gg
import travelfrontend.composeapp.generated.resources.login_background

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
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSigningUp by remember { mutableStateOf(false) }
    var isSigningUpWithGoogle by remember { mutableStateOf(false) }

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
                    if (email.isNotBlank() && password.isNotBlank() && !isSigningUp) {
                        isSigningUp = true
                        coroutineScope.launch {
                            val result = authService.createUserWithEmailAndPassword(email.trim(), password.trim())
                            result.onSuccess {
                                onSignUpSuccess()
                            }.onFailure { exception ->
                                errorMessage = "Error: ${exception.message}"
                            }
                            isSigningUp = false
                        }
                    } else if (!isSigningUp) {
                        errorMessage = "Please fill in both fields."
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
                                onSignUpSuccess()
                            }.onFailure { e ->
                                errorMessage = "Google Sign-Up Failed: ${e.message}"
                            }
                            isSigningUpWithGoogle = false
                        }
                    }
            )

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