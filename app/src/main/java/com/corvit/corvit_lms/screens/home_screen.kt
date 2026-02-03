package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
// ✅ 1. Import the new API Model
import com.corvit.corvit_lms.data.ApiCourse
import com.corvit.corvit_lms.screens.components.shimmerEffect
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState
// ✅ 2. Explicitly import getValue
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    catalogViewModel: CatalogViewModel
) {
    val context = LocalContext.current

    // --- 1. Data Fetching ---
    val userDataState = authViewModel.userDataState.observeAsState()
    LaunchedEffect(Unit) { authViewModel.getUserName() }

    val displayName = when (val state = userDataState.value) {
        is UserDataState.Success -> state.name
        is UserDataState.Loading -> "..."
        else -> "Student"
    }

    // Fetch Courses (Now using ApiCourse)
    // ✅ Make sure this matches your ViewModel variable name (coursesList vs courseslist)
    val allCourses by catalogViewModel.coursesList.collectAsStateWithLifecycle()

    // Filter logic: Get random courses for "Recommended"
    val recommendedCourses = remember(allCourses) {
        allCourses.filter { it.name.isNotEmpty() }
            .shuffled()
            .take(5)
    }

    // Dummy registered courses (Using ApiCourse type now)
    val registeredCourses = remember { emptyList<ApiCourse>() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // --- 2. HEADER SECTION ---
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Welcome back,",
                            fontFamily = Montserrat,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        if (userDataState.value is UserDataState.Loading) {
                            // ✨ Shimmer Box
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(150.dp)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmerEffect()
                            )
                        } else {
                            Text(
                                text = displayName,
                                fontFamily = Montserrat,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Profile Picture Placeholder
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { navController.navigate("profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.take(1).uppercase(),
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = CorvitPrimaryRed
                        )
                    }
                }
            }

            // --- 3. ANNOUNCEMENTS BOX ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CorvitPrimaryRed)
                        .clickable { /* Open Announcements */ }
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "New Announcement",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Final Term Exams Date Sheet has been uploaded. Check your portal.",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // --- 4. QUICK ACCESS GRID ---
            item {
                Text(
                    text = "Quick Access",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickAccessCard(
                            icon = Icons.Default.CalendarToday,
                            title = "Timetable",
                            modifier = Modifier.weight(1f)
                        ) { navController.navigate("timetable") }

                        QuickAccessCard(
                            icon = Icons.Default.Assignment,
                            title = "Assignments",
                            modifier = Modifier.weight(1f)
                        ) { Toast.makeText(context, "Assignments", Toast.LENGTH_SHORT).show() }

                        QuickAccessCard(
                            icon = Icons.Default.BarChart,
                            title = "Results",
                            modifier = Modifier.weight(1f)
                        ) { Toast.makeText(context, "Results", Toast.LENGTH_SHORT).show() }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickAccessCard(
                            icon = Icons.Default.DateRange,
                            title = "Date Sheet",
                            modifier = Modifier.weight(1f)
                        ) { Toast.makeText(context, "Date Sheet", Toast.LENGTH_SHORT).show() }

                        QuickAccessCard(
                            icon = Icons.Default.CheckCircle,
                            title = "Attendance",
                            modifier = Modifier.weight(1f)
                        ) { Toast.makeText(context, "Attendance", Toast.LENGTH_SHORT).show() }

                        QuickAccessCard(
                            icon = Icons.Default.AttachMoney,
                            title = "Fee Challan",
                            modifier = Modifier.weight(1f)
                        ) { Toast.makeText(context, "Fee Challan", Toast.LENGTH_SHORT).show() }
                    }
                }
            }

            // --- 5. REGISTERED COURSES ---
            item {
                SectionHeader("Registered Courses") { /* View All */ }

                if (registeredCourses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No active courses yet.",
                            fontFamily = Montserrat,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(registeredCourses) { course ->
                            RegisteredCourseCard(course)
                        }
                    }
                }
            }

            // --- 6. RECOMMENDED COURSES ---
            item {
                SectionHeader("Recommended for you") { navController.navigate("categories") }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(recommendedCourses) { course ->
                        RecommendedCourseCardSimple(course) {
                            // ✅ Pass ID and Name properly
                            val encodedName = android.net.Uri.encode(course.name)
                            navController.navigate("course_detail/$encodedName/${course.id}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


@Composable
fun QuickAccessCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CorvitPrimaryRed,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontFamily = Montserrat,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, onViewAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "See All",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = CorvitPrimaryRed,
            modifier = Modifier.clickable { onViewAll() }
        )
    }
}

@Composable
fun RegisteredCourseCard(course: ApiCourse) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        androidx.compose.animation.core.animate(
            initialValue = 0f,
            targetValue = 0.4f,
            animationSpec = tween(durationMillis = 1000)
        ) { value, _ ->
            progress = value
        }
    }

    Surface(
        modifier = Modifier
            .width(260.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = course.name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = CorvitPrimaryRed,
                trackColor = CorvitPrimaryRed.copy(alpha = 0.2f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(progress * 100).toInt()}% Completed",
                fontFamily = Montserrat,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun RecommendedCourseCardSimple(course: ApiCourse, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CorvitPrimaryRed.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = CorvitPrimaryRed.copy(alpha = 0.5f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = course.name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = course.deliveryMode ?: "General",
                fontFamily = Montserrat,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}