package com.corvit.corvit_lms.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.corvit.corvit_lms.screens.components.LocalThemeToggleState
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed // Import custom red
import com.corvit.corvit_lms.ui.theme.CorvitSuccessGreen // Import custom green

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ProfileScreen(
    navController: NavController, authViewModel: AuthViewModel
) {
    // Dark Mode State
    val themeToggleState = LocalThemeToggleState.current
    val isDarkTheme = themeToggleState.isDarkTheme
    val onDarkModeToggled = themeToggleState.toggleTheme

    // Other states
    var notificationsEnabled by remember { mutableStateOf(true) }
    val userDataState = authViewModel.userDataState.observeAsState()

    LaunchedEffect(Unit) {
        authViewModel.getUserName()
    }

    val displayName = when (val state = userDataState.value) {
        is UserDataState.Success -> state.name
        is UserDataState.Loading -> "Loading Name..."
        is UserDataState.Error -> "Error"
        else -> "Guest"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            // Fix: Background color handled by the Scaffold wrapping this screen
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        // Fix: Use theme-aware color for avatar background
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
                        // Text color defaults to onBackground/onSurface, but specifying theme is clearer
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Student",
                        fontSize = 15.sp,
                        color = Color.Gray // Gray is often fine in both themes
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
                ClickableItem("Edit Profile")
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
                    // Logout button color remains primary red
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

            AppVersionText(
                version = "1.0.0",
                releaseType = "Stable"
            )
        }
    }
}

// --- Helper Composables ---

@Composable
fun ClickableItem(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        // Fix: Use theme content color
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
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
            // Fix: Use theme content color
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                // Use theme content color for thumb, primary green for track
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
        color = MaterialTheme.colorScheme.onBackground, // Fix: Use theme content color
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            // Fix: Use theme surface variant for cards (light gray in light theme, darker gray in dark theme)
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
            // Fix: Use a light theme-aware color for dividers
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    )
}

@Composable
fun AppVersionText(
    version: String,
    releaseType: String
) {
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