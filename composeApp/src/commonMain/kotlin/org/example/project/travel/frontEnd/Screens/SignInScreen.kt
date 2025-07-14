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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.example.project.travel.frontend.auth.AuthService
import org.example.project.travel.frontend.auth.GoogleSignInManager
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.login_background
import travelfrontend.composeapp.generated.resources.gg

interface SignInScreenComponent {
    val onLoginSuccess: () -> Unit
    val onSignUpClick: () -> Unit
}

class SignInScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    private val authService: AuthService
) : SignInScreenComponent, ComponentContext by componentContext {

    override val onLoginSuccess: () -> Unit = {
        rootComponent.navigateTo(Screen.FlightSearch)
    }

    override val onSignUpClick: () -> Unit = {
        rootComponent.navigateTo(Screen.Signup)
    }
}

@Composable
fun SignInScreen(
    authService: AuthService,
    googleSignInManager: GoogleSignInManager,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var message by remember { mutableStateOf("") }
    var isSigningInWithGoogle by remember { mutableStateOf(false) }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            message = ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
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
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email ID", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = AppBlue,
                        focusedBorderColor = AppBlue,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = AppBlue,
                        focusedBorderColor = AppBlue,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank() && !isSigningIn) {
                            isSigningIn = true
                            coroutineScope.launch {
                                val result = authService.signInWithEmailAndPassword(email.trim(), password.trim())
                                result.onSuccess { userId ->
                                    message = "Sign In Successful!"
                                    onLoginSuccess()
                                }.onFailure { exception ->
                                    message = "Error: ${exception.message}"
                                }
                                isSigningIn = false
                            }
                        } else if (!isSigningIn) {
                            message = "Please enter both email and password."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = !isSigningIn
                ) {
                    Text(if (isSigningIn) "Signing In..." else "Sign In", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "OR SIGN IN WITH",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Image(
                    painter = painterResource(Res.drawable.gg),
                    contentDescription = "Google Sign-In",
                    modifier = Modifier
                        .size(40.dp) // Adjust size as needed
                        .clickable(enabled = !isSigningInWithGoogle) {
                            coroutineScope.launch {
                                isSigningInWithGoogle = true
                                googleSignInManager.signOut()
                                googleSignInManager.signInWithGoogle().onSuccess {
                                    message = "Google Sign In Successful!"
                                    onLoginSuccess()
                                }.onFailure { exception ->
                                    message = "Error: ${exception.message}"

                                }
                                isSigningInWithGoogle = false
                            }
                        }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { onSignUpClick() }
                ) {
                    Text("Not a user? Sign up", color = Color.White)
                }
            }
        }
    }
}