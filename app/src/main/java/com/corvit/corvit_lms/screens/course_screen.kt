package com.corvit.corvit_lms.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.data.ApiCourse
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

enum class CourseFilter { ALL, FREE, PRIVATE, NAVTTC }

@Composable
fun CoursesScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel
    // ❌ REMOVED categoryId parameter
) {
    val allCourses by catalogViewModel.coursesList.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(CourseFilter.ALL) }
    var searchText by remember { mutableStateOf("") }

    // 1. Filter Logic
    val filteredCourses = when (selectedFilter) {
        CourseFilter.ALL -> allCourses
        CourseFilter.FREE -> allCourses.filter { (it.fee ?: 0.0) == 0.0 }
        CourseFilter.PRIVATE -> allCourses.filter { it.type == "Private" }
        CourseFilter.NAVTTC -> allCourses.filter { it.type == "NAVTTC" }
    }

    // 2. Search Logic
    val finalCourses = remember(filteredCourses, searchText) {
        if (searchText.isBlank()) filteredCourses
        else filteredCourses.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Header
            Text(
                text = "All Courses",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            // Search Bar
            CourseSearchBar(
                text = searchText,
                onTextChange = { searchText = it }
            )

            // Filter Row
            CourseFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // Content List
            if (finalCourses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No courses found",
                        fontFamily = Montserrat,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(finalCourses) { course ->
                        CourseCard(
                            course = course,
                            onClick = {
                                val encodedName = android.net.Uri.encode(course.name)
                                navController.navigate("course_detail/$encodedName/${course.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: ApiCourse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                if (course.imagePath.isNullOrEmpty()) {
                    // Fallback Image
                    Image(
                        painter = painterResource(id = R.drawable.demo), // Ensure you have a placeholder in drawable
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Real API Image
                    AsyncImage(
                        model = course.getFullImageUrl(), // Use the helper we made
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // Type Badge (Private / NAVTTC)
                Text(
                    text = course.type ?: "Course",
                    fontSize = 11.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(
                            CorvitPrimaryRed.copy(alpha = 0.9f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = course.name,
                    fontSize = 18.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Info Row
                val infoText = buildString {
                    append(course.deliveryMode ?: "Physical")
                    append(" • ")
                    append("${course.durationInWeeks ?: 0} Weeks")
                }
                Text(
                    text = infoText,
                    fontSize = 12.sp,
                    fontFamily = Montserrat,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price
                val price = course.fee ?: 0.0
                val priceText = if (price == 0.0) "Free / Funded" else "Rs. ${String.format("%,.0f", price)}"

                Text(
                    text = priceText,
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = if (price == 0.0) Color(0xFF4CAF50) else CorvitPrimaryRed
                )
            }
        }
    }
}

// Helper Components
@Composable
fun CourseSearchBar(text: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        singleLine = true,
        placeholder = { Text("Search...", fontFamily = Montserrat) },
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = CorvitPrimaryRed
        )
    )
}

@Composable
fun CourseFilterRow(selectedFilter: CourseFilter, onFilterSelected: (CourseFilter) -> Unit) {
    val filters = listOf(
        CourseFilter.ALL to "All",
        CourseFilter.FREE to "Free/Funded",
        CourseFilter.PRIVATE to "Paid Courses",
        CourseFilter.NAVTTC to "NAVTTC"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { (filter, label) ->
            val isSelected = filter == selectedFilter
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Montserrat,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) CorvitPrimaryRed else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}