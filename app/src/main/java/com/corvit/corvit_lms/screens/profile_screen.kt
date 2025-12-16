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
import com.corvit.corvit_lms.viewmodel.UserDataState // Import UserDataState
import com.corvit.corvit_lms.R

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ProfileScreen(
    navController: NavController, authViewModel: AuthViewModel
) {
    // Dark Mode Logic
    val themeToggleState = LocalThemeToggleState.current
    val isDarkTheme = themeToggleState.isDarkTheme
    val onDarkModeToggled = themeToggleState.toggleTheme

    // Notifications State
    var notificationsEnabled by remember { mutableStateOf(true) }

    // 🔥 Firebase User Data Logic
    val userDataState = authViewModel.userDataState.observeAsState()

    // 🔥 Trigger the name fetch when the screen is first composed
    LaunchedEffect(Unit) {
        authViewModel.getUserName()
    }

    // Determine the display name based on the state
    val displayName = when (val state = userDataState.value) {
        is UserDataState.Success -> state.name
        is UserDataState.Loading -> "Loading Name..."
        is UserDataState.Error -> "###"
        else -> "Guest"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop, // Crop to fill the circular shape
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray) // This background will only show if the image itself is transparent
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    // 🔥 Display the dynamic user name here
                    Text(
                        text = displayName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Student",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // ... (rest of the screen content remains the same) ...

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
                    onCheckedChange = {
                        notificationsEnabled = it
                    }
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
                    .background(Color(0xFFE53935))
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

@Composable
fun ClickableItem(
    title: String,
) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
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
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50)
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
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF6F6F6))
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
            .background(Color(0xFFDDDDDD))
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