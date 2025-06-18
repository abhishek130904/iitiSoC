package org.example.project.travel.frontend.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.example.project.travel.frontend.navigation.RootComponent
import org.example.project.travel.frontend.navigation.Screen
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.login_background

interface LoginScreenComponent {
    val onLoginSuccess: () -> Unit
    val onSignUpClick: () -> Unit
}

class SignInScreenComponentImpl(
    componentContext: ComponentContext,
    private val rootComponent: RootComponent,
    private val authService: AuthService // Add AuthService for Firebase
) : SignInScreenComponent, ComponentContext by componentContext {

    override val onLoginSuccess: () -> Unit = {
        rootComponent.navigateTo(Screen.FlightSearch)
    }

    override val onSignUpClick: () -> Unit = {
        rootComponent.navigateTo(Screen.Signup) // Fixed from Screen.SignInScreen to Screen.Signup
    }
}

@Composable
fun LoginPage(
    authService: AuthService,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val blueColor = Color(23, 111, 243)
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var message by remember { mutableStateOf("") }

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
                    text = "Login",
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
                        .padding(vertical = 8.dp)
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
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            coroutineScope.launch {
                                val result = authService.signInWithEmailAndPassword(email.trim(), password.trim())
                                result.onSuccess { userId ->
                                    message = "Login Successful!"
                                    onLoginSuccess()
                                }.onFailure { exception ->
                                    message = "Error: ${exception.message}"
                                }
                            }
                        } else {
                            message = "Please enter both email and password."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Login", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { onSignUpClick() }
                ) {
                    Text("Not a user? Sign up", color = Color.White) // Fixed text to match intent
                }
            }
        }
    }
}