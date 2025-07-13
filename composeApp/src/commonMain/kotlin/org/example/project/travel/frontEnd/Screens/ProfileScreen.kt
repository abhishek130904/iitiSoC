package org.example.project.travel.frontEnd.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.travel.frontend.auth.UserProfile
import org.example.project.travel.frontend.auth.fetchUserProfile
import org.example.project.travel.frontend.auth.AuthService
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.painterResource
import travelfrontend.composeapp.generated.resources.Res
import travelfrontend.composeapp.generated.resources.background_image
import travelfrontend.composeapp.generated.resources.gg

// Custom colors based on the provided blue (RGB: 23, 111, 243)
val AppBlue = Color(23, 111, 243)
val LightBlue = Color(200, 220, 255)
val DarkGray = Color(51, 51, 51)

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    uid: String?,
    authService: AuthService,
    password: String,
    onLogout: suspend () -> Unit = {},
    onMyTripsClick: ((String) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editAge by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    // Fetch profile on first composition or when uid changes
    LaunchedEffect(uid) {
        isVisible = true
        if (uid != null) {
            userProfile = fetchUserProfile(uid)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background Image with Overlay
        Image(
            painter = painterResource(Res.drawable.background_image),
            contentDescription = "Profile Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//             Profile Image Placeholder
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(LightBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Profile",
                    modifier = Modifier.size(100.dp),
                    tint = AppBlue
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Profile Details
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppBlue,
                            fontWeight = FontWeight.Bold
                        )

                        Divider(
                            color = AppBlue.copy(alpha = 0.3f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        when {
                            userProfile == null -> {
                                CircularProgressIndicator(
                                    color = AppBlue,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = "Loading profile...",
                                    color = DarkGray,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            else -> {
                                Spacer(modifier = Modifier.height(16.dp))
                                ProfileDetailItem("Name", userProfile!!.name, AppBlue)
                                ProfileDetailItem("Email", userProfile!!.email, DarkGray)
                                ProfileDetailItem("Age", "${userProfile!!.age}", DarkGray)
                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = {
                                        editName = userProfile!!.name
                                        editAge = userProfile!!.age.toString()
                                        showEditDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Edit Profile",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Large, visible Logout Button below the card
            Button(
                onClick = {
                    coroutineScope.launch {
                        authService.signOut()
                        onLogout()
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Bottom Bar (like TripConfirmationScreen)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp, start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onHomeClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = AppBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Home", color = AppBlue, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                }
                // My Trips Button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (uid != null && onMyTripsClick != null) {
                                onMyTripsClick(uid)
                            }
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "My Trips",
                        tint = AppBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("My Trips", color = AppBlue, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                }
            }
        }

        // Edit Dialog
        if (showEditDialog && userProfile != null && uid != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Profile") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Name") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editAge,
                            onValueChange = { editAge = it },
                            label = { Text("Age") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val newAge = editAge.toIntOrNull()
                            if (editName.isNotBlank() && newAge != null) {
                                isSaving = true
                                coroutineScope.launch {
                                    authService.saveUserProfile(uid, editName.trim(), newAge, userProfile!!.email)
                                    userProfile = fetchUserProfile(uid)
                                    isSaving = false
                                    showEditDialog = false
                                }
                            }
                        },
                        enabled = !isSaving
                    ) { Text(if (isSaving) "Saving..." else "Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
private fun ProfileDetailItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            color = AppBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}