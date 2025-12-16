package com.corvit.corvit_lms.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.corvit.corvit_lms.data.Course // Assuming Course data class is defined
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@Composable
fun CoursesScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    categoryId: String
) {
    // Collect courses state
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()

    // Filter and sort courses
    val filteredCourses = allCourses.filter { it.category_id == categoryId }
    val courses = filteredCourses.sortedBy { it.name }

    if (courses.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Use theme-aware color
            Text(
                text = "No courses found for this category",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        // Fix: Use theme background for LazyColumn's background if needed
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(courses) { course ->
                CourseCard(course = course)
            }
        }
    }
}

@Composable
fun CourseCardWide(
    name: String,
    certification: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp)
        // Card uses theme-aware surface color by default
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            // Thumbnail
            Image(
                painter = painterResource(id = R.drawable.demo),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                // Course Title
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    // Use theme-aware content color
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Level - Duration - Batch
                Text(
                    text = "Intermediate • 2 Months • Hybrid",
                    fontSize = 13.sp,
                    // Use a slightly desaturated theme-aware color
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Certification Badge
            Text(
                text = if (certification) "Certified" else "✖ No Cert",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                // Use theme-aware content color
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun CourseCard(
    course : Course
) {
    // Card uses theme-aware surface color by default
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

                // Gradient overlay remains dark to ensure white text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )

                // Certification badge
                Text(
                    text = if (course.certification) "Certified" else "No Certification",
                    fontSize = 12.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        // Use a dark, theme-aware transparent color for the background box
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Course Name
            Text(
                text = course.name,
                fontSize = 18.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                // Use theme-aware content color
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
                // Use a slightly desaturated theme-aware color
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price Row
            val regularPrice = course.prices?.regular_pkr?.let {
                "Fee: Rs. ${String.format("%,.0f", it)}"
            } ?: "Price N/A"

            Text(
                text = regularPrice,
                fontSize = 15.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                // Fix: Use theme-aware green. Use primary container or success color
                color = Color(0xFF4CAF50), // Using a standard green that looks good on both light/dark surfaces
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}