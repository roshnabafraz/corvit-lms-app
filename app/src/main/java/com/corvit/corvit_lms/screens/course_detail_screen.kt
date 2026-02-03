package com.corvit.corvit_lms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import com.corvit.corvit_lms.data.ApiCourse

@Composable
fun CourseDetailScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    courseName: String, // Kept for header/fallback
    courseId: Int       // ✅ Added ID for accurate lookup
) {
    // 1. Collect the list from ViewModel
    // Make sure your ViewModel property is named 'coursesList' (camelCase)
    val allCourses by catalogViewModel.coursesList.collectAsStateWithLifecycle()

    // 2. Find the course by ID (More accurate than name)
    val course = allCourses.firstOrNull { it.id == courseId }

    if (course == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CorvitPrimaryRed)
        }
        return
    }

    // 3. Map API Fields to UI Logic
    val fee = course.fee ?: 0.0
    val durationText = "${course.durationInWeeks ?: 0} Weeks"
    val deliveryMode = course.deliveryMode ?: "Physical"
    val type = course.type ?: "Private"

    // Default description if API returns null
    val description = course.description ?: """
        ${course.name} is designed to help you build strong, practical skills. 
        You’ll learn core concepts with real-world examples and industry-style practices.
    """.trimIndent()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total Fee",
                            fontFamily = Montserrat,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = if (fee == 0.0) "Free" else "Rs. ${String.format("%,.0f", fee)}",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = CorvitPrimaryRed
                        )
                    }

                    Button(
                        onClick = {
                            val encodedName = android.net.Uri.encode(course.name)
                            // Navigate with both Name and ID
                            navController.navigate("enrollment/$encodedName/${course.id}")
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .width(160.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CorvitPrimaryRed)
                    ) {
                        Text(
                            text = "Enroll Now",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // --- HEADER IMAGE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Background Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                }

                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 100f
                            )
                        )
                )

                // Back Button
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .statusBarsPadding()
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Title & Vendor
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = type, // e.g. "Private" or "NAVTTC"
                        color = CorvitPrimaryRed,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = course.name,
                        color = Color.White,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 30.sp
                    )
                }
            }

            // --- CONTENT ---
            Column(modifier = Modifier.padding(20.dp)) {

                // Info Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Mapped to new API fields
                    DetailChip(Icons.Default.DateRange, durationText)
                    DetailChip(Icons.Default.Person, deliveryMode)
                    DetailChip(Icons.Default.Info, if(fee == 0.0) "Funded" else "Paid")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 1. KEY HIGHLIGHTS
                Text(
                    text = "Key Highlights",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    DetailItem("Hours", "${course.totalHours ?: 0} Hrs", true)
                    VerticalDivider()
                    DetailItem("Mode", deliveryMode, false)
                    VerticalDivider()
                    DetailItem("Type", type, false)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. ABOUT THIS COURSE
                Text(
                    text = "About this Course",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontFamily = Montserrat,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

                // Syllabus Section (If available)
                if (!course.syllabus.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Syllabus Overview",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = course.syllabus,
                        fontFamily = Montserrat,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color.Gray.copy(0.3f))
    )
}

@Composable
fun DetailChip(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CorvitPrimaryRed,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontFamily = Montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DetailItem(title: String, value: String, highlight: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontFamily = Montserrat, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (highlight) CorvitPrimaryRed else MaterialTheme.colorScheme.onSurface
        )
    }
}