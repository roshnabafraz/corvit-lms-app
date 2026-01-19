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
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.data.Course
import com.corvit.corvit_lms.data.CourseFilter
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@Composable
fun CoursesScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    categoryId: String
) {
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(CourseFilter.ALL) }
    var searchText by remember { mutableStateOf("") }
    var appliedQuery by remember { mutableStateOf("") }

    // Logic: Filter by Category -> Filter by Type -> Search
    val categoryCourses = allCourses.filter { it.category_id == categoryId }

    val filteredCourses = when (selectedFilter) {
        CourseFilter.ALL -> categoryCourses
        CourseFilter.FREE -> categoryCourses.filter { it.prices?.regular_pkr == 0.0 }
        CourseFilter.CERTIFIED -> categoryCourses.filter { it.certification }
        CourseFilter.PRICE_LOW_HIGH -> categoryCourses.sortedBy { it.prices?.regular_pkr ?: Double.MAX_VALUE }
        CourseFilter.PRICE_HIGH_LOW -> categoryCourses.sortedByDescending { it.prices?.regular_pkr ?: 0.0 }
        else -> categoryCourses
    }

    val finalCourses = remember(filteredCourses, appliedQuery) {
        val q = appliedQuery.trim()
        if (q.isEmpty()) filteredCourses
        else filteredCourses.filter { it.name.contains(q, ignoreCase = true) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Search Bar
            CourseSearchBar(
                text = searchText,
                onTextChange = { searchText = it },
                onSearchClick = { appliedQuery = searchText }
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
                    val msg = if (appliedQuery.isNotEmpty()) "No results for \"$appliedQuery\"" else "No courses found"
                    Text(
                        text = msg,
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
                                // 🔥 FIX: Use 'course.name' instead of 'category_id'
                                // Since you don't have an ID, Name is the only unique way to distinguish them
                                navController.navigate("course_detail/${android.net.Uri.encode(course.name)}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// ... (Rest of your Composable functions: CourseSearchBar, CourseFilterRow, CourseCard stay exactly the same)
@Composable
private fun CourseSearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(
            fontFamily = Montserrat,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        ),
        placeholder = {
            Text(
                text = "Search course...",
                fontFamily = Montserrat,
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        shape = RoundedCornerShape(50.dp),
        trailingIcon = {
            Button(
                onClick = onSearchClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CorvitPrimaryRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = "Search",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CourseFilterRow(
    selectedFilter: CourseFilter,
    onFilterSelected: (CourseFilter) -> Unit
) {
    val filters = listOf(
        CourseFilter.ALL to "All",
        CourseFilter.CERTIFIED to "Certified",
        CourseFilter.PRICE_LOW_HIGH to "Price: Low → High",
        CourseFilter.PRICE_HIGH_LOW to "Price: High → Low"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
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
                    .background(
                        if (isSelected) CorvitPrimaryRed else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.demo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
                if (course.certification) {
                    Text(
                        text = "Certified",
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
            }

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
                val infoText = buildString {
                    append(course.courseLevel ?: "N/A")
                    append(" • ")
                    append(course.duration ?: "N/A")
                    append(" • ")
                    append(course.batchType ?: "N/A")
                }
                Text(
                    text = infoText,
                    fontSize = 12.sp,
                    fontFamily = Montserrat,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                val price = course.prices?.regular_pkr ?: 0.0
                val priceText = if (price == 0.0) "Free" else "Rs. ${String.format("%,.0f", price)}"
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