package com.corvit.corvit_lms.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    val categoryCourses = allCourses.filter {
        it.category_id == categoryId
    }

    // 🔹 Logic updated: Level filters removed
    val filteredCourses = when (selectedFilter) {
        CourseFilter.ALL -> categoryCourses

        CourseFilter.FREE ->
            categoryCourses.filter { it.prices?.regular_pkr == 0.0 }

        CourseFilter.CERTIFIED ->
            categoryCourses.filter { it.certification }

        CourseFilter.PRICE_LOW_HIGH ->
            categoryCourses.sortedBy { it.prices?.regular_pkr ?: Double.MAX_VALUE }

        CourseFilter.PRICE_HIGH_LOW ->
            categoryCourses.sortedByDescending { it.prices?.regular_pkr ?: 0.0 }

        else -> categoryCourses // Handles any unused filter states safely
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔹 Filter Row
        CourseFilterRow(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        if (filteredCourses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No courses found",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCourses) { course ->
                    CourseCard(course = course)
                }
            }
        }
    }
}

@Composable
fun CourseFilterRow(
    selectedFilter: CourseFilter,
    onFilterSelected: (CourseFilter) -> Unit
) {

    // 🔹 Removed Beginner, Intermediate, and Expert from this list
    val filters = listOf(
        CourseFilter.ALL to "All",
        CourseFilter.CERTIFIED to "Certified",
        CourseFilter.PRICE_LOW_HIGH to "Price: Low → High",
        CourseFilter.PRICE_HIGH_LOW to "Price: High → Low"
    )

    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { (filter, label) ->
            val isSelected = filter == selectedFilter

            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CourseCard(
    course : Course
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
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
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )

                Text(
                    text = if (course.certification) "Certified" else "No Certification",
                    fontSize = 12.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = course.name,
                fontSize = 18.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Info Row: Level • Duration • Batch
            val infoText = buildString {
                append(course.courseLevel ?: "N/A Level")
                append(" • ")
                append(course.duration ?: "N/A Duration")
                append(" • ")
                append(course.batchType ?: "N/A Batch")
            }
            Text(
                text = infoText,
                fontSize = 13.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            val regularPrice = course.prices?.regular_pkr?.let {
                "Fee: Rs. ${String.format("%,.0f", it)}"
            } ?: "Price N/A"

            Text(
                text = regularPrice,
                fontSize = 15.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}