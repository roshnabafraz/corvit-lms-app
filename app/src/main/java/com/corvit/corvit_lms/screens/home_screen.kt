package com.corvit.corvit_lms.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import androidx.compose.foundation.isSystemInDarkTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){
    // This function can be simplified if the one below is the primary home screen
}

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit = {},
    onContinueCourseClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onAssignmentsClick: () -> Unit = {},
    onLiveClassesClick: () -> Unit = {},
    onCertificatesClick: () -> Unit = {},
    onAnnouncementClick: () -> Unit = {}
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    val isCurrentThemeLight = !isSystemInDarkTheme()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome back,",
                        fontSize = 14.sp,
                        color = Color.Gray // Gray is fine across themes
                    )
                    Text(
                        text = "Roshnab",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor // Fix: Use theme content color
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        // Fix: Use theme surface variant color
                        .background(surfaceColor)
                        .clickable { onProfileClick() }
                )
            }
        }

        // Continue Learning
        item {
            SectionTitle("Continue Learning")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    // Fix: Use theme surface variant color
                    .background(surfaceColor)
                    .clickable { onContinueCourseClick() }
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Android Development",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = contentColor // Fix: Use theme content color
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Lesson 5 • Jetpack Compose",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Categories
        item {
            SectionTitle("Categories")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryChip("Mobile Dev") { onCategoryClick("Mobile Dev") }
                CategoryChip("Web Dev") { onCategoryClick("Web Dev") }
                CategoryChip("Cloud") { onCategoryClick("Cloud") }
                CategoryChip("AI / ML") { onCategoryClick("AI / ML") }
            }
        }

        // Quick Access
        item {
            SectionTitle("Quick Access")
            // Replaced SectionCard with the correct composable if SectionRow is the intention
            SectionRow {
                QuickActionItem("Assignments", onAssignmentsClick)
                DividerItem()
                QuickActionItem("Live Classes", onLiveClassesClick)
                DividerItem()
                QuickActionItem("Certificates", onCertificatesClick)
            }
        }

        // Announcements
        item {
            SectionTitle("Announcements")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    // 🔥 FIX APPLIED HERE: Use the local theme state check
                    .background(if (isCurrentThemeLight) Color(0xFFEFF3FF) else Color(0xFF2C3E50))
                    .clickable { onAnnouncementClick() }
                    .padding(16.dp)
            ) {
                Text(
                    text = "New Cloud Computing batch starts next week.",
                    fontSize = 14.sp,
                    // 🔥 FIX APPLIED HERE: Use the local theme state check
                    color = if (isCurrentThemeLight) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            // Fix: Use theme surface variant for chip background
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface // Fix: Use theme content color
        )
    }
}

@Composable
fun QuickActionItem(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 15.sp,
        // Fix: Use theme content color
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    )
}

// Renamed from SectionRow to avoid confusion with SectionCard/SectionRow from ProfileScreen
@Composable
fun SectionRow(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            // Fix: Use theme surface variant color
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp)
    ) {
        content()
    }
}