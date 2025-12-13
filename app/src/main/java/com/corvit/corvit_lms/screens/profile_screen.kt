package com.corvit.corvit_lms.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import com.corvit.corvit_lms.ui.theme.Montserrat

@Preview(showSystemUi = true)
@Composable
fun ProfileScreen(
    onMyCoursesClick: () -> Unit = {},
    onCertificatesClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDarkModeChanged: (Boolean) -> Unit = {},
    onNotificationsChanged: (Boolean) -> Unit = {}
) {

    var darkModeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Header - Horizontal Layout
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Name & Role
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Roshnab Afraz",
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


        // Quick Access
        item {
            SectionTitle("Quick Access")
            SectionCard {
                ClickableItem("My Courses", onMyCoursesClick)
                DividerItem()
                ClickableItem("Certificates", onCertificatesClick)
                DividerItem()
                ClickableItem("Payments", onPaymentsClick)
            }
        }

        // Settings
        item {
            SectionTitle("Settings")
            SectionCard {

                ClickableItem("Edit Profile", onEditProfileClick)
                DividerItem()

                ToggleItem(
                    title = "Notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        onNotificationsChanged(it)
                    }
                )

                DividerItem()

                ToggleItem(
                    title = "Dark Mode",
                    checked = darkModeEnabled,
                    onCheckedChange = {
                        darkModeEnabled = it
                        onDarkModeChanged(it)
                    }
                )

                DividerItem()

                ClickableItem("Help & Support", onHelpClick)
            }
        }

        // Logout
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE53935))
                    .clickable { onLogoutClick() }
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
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
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
    releaseType: String // "Stable" or "Beta"
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