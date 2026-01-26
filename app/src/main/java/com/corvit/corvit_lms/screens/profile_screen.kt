package com.corvit.corvit_lms.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ProfileScreen(
    navController: NavController, authViewModel: AuthViewModel, userName: String
) {
    val context = LocalContext.current

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

    // --- EDIT NAME DIALOG ---
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

        // 1. Profile Header
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

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = userName,
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

        // 2. Contact Buttons (WhatsApp & Location)
        item {
            SectionTitle("Contact Us")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // WhatsApp Button
                ContactActionButton(
                    text = "WhatsApp",
                    icon = Icons.Filled.Send, // Use Icons.Default.Call or custom drawable if preferred
                    backgroundColor = Color(0xFF25D366), // Official WhatsApp Green
                    modifier = Modifier.weight(1f),
                    onClick = { openWhatsApp(context) }
                )

                // Google Maps Button
                ContactActionButton(
                    text = "Location",
                    icon = Icons.Filled.LocationOn,
                    backgroundColor = Color(0xFF4285F4), // Official Google Blue
                    modifier = Modifier.weight(1f),
                    onClick = { openGoogleMaps(context) }
                )
            }
        }

        // 3. Quick Access
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

        // 4. Settings
        item {
            SectionTitle("Settings")
            SectionCard {
                ClickableItem("Edit Profile") {
                    nameToUpdate = if(userName != "Loading Name..." && userName != "Guest") userName else ""
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

        // 5. Logout
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
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- Intent Helper Functions ---

fun openWhatsApp(context: Context) {
    // Replace with Corvit's official number
    val phoneNumber = "+923038888555"
    val message = "Hi Corvit, I have a query regarding a course."
    val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
    }
}

fun openGoogleMaps(context: Context) {
    // Coordinates for Corvit Systems Lahore
    val gmmIntentUri = Uri.parse("geo:31.5204,74.3587?q=Corvit+Systems+Lahore")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(mapIntent)
    } catch (e: Exception) {
        // If Google Maps app is not installed, open in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=Corvit+Systems+Lahore"))
        context.startActivity(browserIntent)
    }
}


// --- Helper Composables ---

@Composable
fun ContactActionButton(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

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
            .clickable { onClick() }
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