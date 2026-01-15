package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.data.Course
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(
    navController: NavController,
    authViewModel: AuthViewModel,
    catalogViewModel: CatalogViewModel
) {
    val context = LocalContext.current

    // ✅ Real-time profile name
    val userDataState = authViewModel.userDataState.observeAsState()
    LaunchedEffect(Unit) { authViewModel.getUserName() }

    val displayName = when (val state = userDataState.value) {
        is UserDataState.Success -> state.name
        is UserDataState.Loading -> "Loading..."
        is UserDataState.Error -> "User"
        else -> "User"
    }

    // ✅ Firebase courses
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()
    val safeCourses = remember(allCourses) { allCourses.filter { !it.name.isNullOrBlank() } }

    // ✅ Recommendations
    val recommendations = remember(safeCourses) {
        val certified = safeCourses.filter { it.certification }
        val paid = safeCourses.filter { (it.prices?.regular_pkr ?: 0.0) > 0.0 }
        (certified + paid).distinctBy { it.id }.take(8)
    }

    // ✅ Registered courses placeholder (later connect with user enrollments)
    val registeredCourses: List<Course> = emptyList()

    // ✅ Announcements (demo for now)
    val announcements = remember {
        listOf(
            "New batch starts next week. Enroll early to reserve seat.",
            "Fee discount offer available for limited time.",
            "Weekend classes schedule updated (check timetable)."
        )
    }

    Scaffold(

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ✅ Greeting row (name left + circle right, parallel)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hi, $displayName ",
                            fontFamily = Montserrat,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(4.dp))

                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { navController.navigate("profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.trim().take(1).uppercase(),
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // ✅ Registered Courses
            item {
                SectionHeaderRow(
                    title = "Registered Courses",
                    actionText = "View All",
                    onActionClick = {
                        Toast.makeText(context, "Registered courses (coming soon)", Toast.LENGTH_SHORT).show()
                        // Later: navController.navigate("my_courses")
                    }
                )
            }

            item {
                if (registeredCourses.isEmpty()) {
                    EmptyStrip("No registered courses yet.")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(registeredCourses) { c ->
                            CourseMiniCard(
                                course = c,
                                onClick = {
                                    navController.navigate("course_detail/${android.net.Uri.encode(c.id)}")
                                }
                            )
                        }
                    }
                }
            }

            // ✅ Recommendations (Firebase)
            item {
                SectionHeaderRow(
                    title = "Recommendations",
                    actionText = "View All",
                    onActionClick = {
                        navController.navigate("course_screen") // ✅ your route
                    }
                )
            }

            item {
                if (recommendations.isEmpty()) {
                    EmptyStrip("No recommendations yet.")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(recommendations) { c ->
                            RecommendedCourseCardFirebase(
                                course = c,
                                onClick = {
                                    navController.navigate("course_detail/${android.net.Uri.encode(c.id)}")
                                }
                            )
                        }
                    }
                }
            }

            // ✅ Announcements
            item {
                SectionHeaderRow(
                    title = "Announcements",
                    actionText = "",
                    onActionClick = {}
                )
            }

            item {
                AnnouncementsCard(items = announcements)
            }

            item { Spacer(Modifier.height(6.dp)) }
        }
    }
}


/* ---------- Small UI Components ---------- */

@Composable
private fun SectionHeaderRow(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.weight(1f))
        if (actionText.isNotBlank()) {
            Text(
                text = actionText,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
private fun EmptyStrip(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(14.dp),
            fontFamily = Montserrat,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun CourseMiniCard(course: Course, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            )
            Text(
                text = course.name ?: "Course",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RecommendedCourseCardFirebase(course: Course, onClick: () -> Unit) {
    val priceText = course.prices?.regular_pkr?.let {
        if (it == 0.0) "Free" else "Rs. ${String.format("%,.0f", it)}"
    } ?: "Price N/A"

    Surface(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            )
            Column(Modifier.padding(14.dp)) {
                Text(
                    text = course.name ?: "Course",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = course.vendor ?: "Corvit",
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f)
                    )
                    Spacer(Modifier.width(10.dp))
                    MetaPill(priceText)
                    if (course.certification) {
                        Spacer(Modifier.width(8.dp))
                        MetaPill("Certified")
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetaPill(course.courseLevel ?: "Level N/A")
                    MetaPill(course.duration ?: "Duration N/A")
                }
            }
        }
    }
}

@Composable
private fun MetaPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontFamily = Montserrat,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            maxLines = 1
        )
    }
}

@Composable
private fun AnnouncementsCard(items: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { msg ->
                Text(
                    text = "• $msg",
                    fontFamily = Montserrat,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            }
        }
    }
}
