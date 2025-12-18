package com.corvit.corvit_lms.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.screens.components.LocalThemeToggleState
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.CorvitSuccessGreen
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ProfileScreen(
    navController: NavController, authViewModel: AuthViewModel
) {
    // Dark Mode State
    val themeToggleState = LocalThemeToggleState.current
    val isDarkTheme = themeToggleState.isDarkTheme
    val onDarkModeToggled = themeToggleState.toggleTheme

    // User Data State
    val userDataState = authViewModel.userDataState.observeAsState()

    // Dialog States
    var showEditNameDialog by remember { mutableStateOf(false) }
    var nameToUpdate by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    // Fetch name on load
    LaunchedEffect(Unit) {
        authViewModel.getUserName()
    }

    // Determine current display name
    val displayName = when (val state = userDataState.value) {
        is UserDataState.Success -> state.name
        is UserDataState.Loading -> "Loading Name..."
        is UserDataState.Error -> "Error"
        else -> "Guest"
    }

    // --- DIALOG FOR EDITING NAME ---
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Edit Profile",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your new name below:",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = nameToUpdate,
                        onValueChange = { nameToUpdate = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nameToUpdate.isNotBlank()) {
                            authViewModel.updateUserName(nameToUpdate)
                            showEditNameDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorvitPrimaryRed)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    // --- MAIN UI ---
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = displayName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Student",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            SectionTitle("Quick Access")
            SectionCard {
                ClickableItem("My Courses")
                DividerItem()
                ClickableItem("Certificates")
                DividerItem()
                ClickableItem("Payments")
            }
        }

        item {
            SectionTitle("Settings")
            SectionCard {
                // Modified to trigger Dialog
                ClickableItem("Edit Profile") {
                    nameToUpdate = if(displayName != "Loading Name..." && displayName != "Guest") displayName else ""
                    showEditNameDialog = true
                }

                DividerItem()

                ToggleItem(
                    title = "Notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                DividerItem()

                ToggleItem(
                    title = "Dark Mode",
                    checked = isDarkTheme,
                    onCheckedChange = onDarkModeToggled
                )
                DividerItem()

                ClickableItem("Help & Support")
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CorvitPrimaryRed)
                    .clickable {
                        authViewModel.logout()
                        navController.navigate("login")
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            AppVersionText("1.0.0", "Stable")
        }
    }
}

// --- Helper Composables (Updated) ---

@Composable
fun ClickableItem(title: String, onClick: () -> Unit = {}) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Updated to use the lambda
            .padding(horizontal = 16.dp, vertical = 14.dp)
    )
}

@Composable
fun ToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = CorvitSuccessGreen
            )
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun DividerItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(0.8.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    )
}

@Composable
fun AppVersionText(version: String, releaseType: String) {
    Text(
        text = "App Version $version ($releaseType)",
        fontSize = 14.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}