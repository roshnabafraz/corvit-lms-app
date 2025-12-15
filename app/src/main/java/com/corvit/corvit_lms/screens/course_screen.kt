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
import com.corvit.corvit_lms.data.Course
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@Composable
fun CoursesScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    categoryId: String
) {

    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()

    val filteredCourses = allCourses.filter { it.category_id == categoryId }

    val courses = filteredCourses.sortedBy { it.name }

    if (courses.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No courses found for this category")
        }
    } else {
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
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Level - Duration - Batch
                Text(
                    text = "Intermediate • 2 Months • Hybrid",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            // Certification Badge
            Text(
                text = if (certification) "Certified" else "✖ No Cert",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
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

                // Gradient overlay
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
                    text = if (course.certification) "Certified" else "No Certification", // 🌟 ACTUAL DATA
                    fontSize = 12.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Course Name
            Text(
                text = course.name, // 🌟 ACTUAL DATA
                fontSize = 18.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Info Row: Level • Duration • Batch
            // 🌟 ACTUAL DATA: Constructs the info string
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
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price Row
            // 🌟 ACTUAL DATA: Displaying the main regular PKR price
            val regularPrice = course.prices?.regular_pkr?.let {
                // Simple formatting for display (e.g., 18000.0 -> Rs. 18,000)
                "Fee: Rs. ${String.format("%,.0f", it)}"
            } ?: "Price N/A"

            Text(
                text = regularPrice,
                fontSize = 15.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF006400), // Dark green for price
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}