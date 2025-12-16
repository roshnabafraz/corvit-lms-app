package com.corvit.corvit_lms.screens

import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.others.YouTubePlayer
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val categories by catalogViewModel.categorylist.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
            }
            else -> {
                // Handle other states if needed
            }
        }
    }

    //YouTubePlayer(videoId = "1_w8QEeYZT8", lifecycleOwner = LocalLifecycleOwner.current)

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
                        color = Color.Gray
                    )
                    Text(
                        text = "Roshnab",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
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
                    .background(Color(0xFFF6F6F6))
                    .clickable { onContinueCourseClick() }
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Android Development",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
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

            SectionCard {
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
                    .background(Color(0xFFEFF3FF))
                    .clickable { onAnnouncementClick() }
                    .padding(16.dp)
            ) {
                Text(
                    text = "New Cloud Computing batch starts next week.",
                    fontSize = 14.sp
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
            .background(Color(0xFFF0F0F0))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = title, fontSize = 13.sp)
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    )
}

@Composable
fun SectionRow(content: @Composable ColumnScope.() -> Unit) {
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


