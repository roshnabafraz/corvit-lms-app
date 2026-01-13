package com.corvit.corvit_lms.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@Composable
fun CourseDetailScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    courseId: String
) {
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()
    val course = allCourses.firstOrNull { it.id == courseId }

    if (course == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    // Hardcoded demo description (2–3 paragraphs)
    val description = remember(course.name) {
        """
        ${course.name} is designed to help you build strong, practical skills with a clear learning path. You’ll learn core concepts with real-world examples and industry-style practices.

        This course includes structured learning modules, guided practice, and key takeaways that help you apply knowledge in projects and assessments. You’ll also get a clear understanding of best practices used by professionals.

        By the end, you’ll be able to confidently explain the main topics and implement them in real scenarios. This course is ideal if you want to grow your skills and build a solid portfolio.
        """.trimIndent()
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    Toast.makeText(
                        navController.context,
                        "Enrolled in ${course.name}  (demo)",
                        Toast.LENGTH_SHORT
                    ).show()

                    //  open demo screen
                    navController.navigate("enroll_demo/${android.net.Uri.encode(course.id)}")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("ENROLL", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ✅ Header banner + back + title (no topbar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.90f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.70f)
                            )
                        )
                    )
            ) {
                // Back button (simple text, no icons)
                Text(
                    text = "← Back",
                    color = Color.White,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clickable { navController.popBackStack() }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = course.vendor ?: "Corvit",
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = course.name,
                        color = Color.White,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        maxLines = 2
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Info chips
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AssistChip(onClick = {}, label = { Text(course.courseLevel ?: "N/A Level") })
                AssistChip(onClick = {}, label = { Text(course.duration ?: "N/A Duration") })
                AssistChip(onClick = {}, label = { Text(course.batchType ?: "N/A Batch") })
            }

            Spacer(Modifier.height(14.dp))

            // Price + certification
            val regularPrice = course.prices?.regular_pkr?.let {
                "Fee: Rs. ${String.format("%,.0f", it)}"
            } ?: "Price N/A"
            val cert = if (course.certification) "Certified" else "No Certification"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(regularPrice, fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(cert, fontFamily = Montserrat, color = MaterialTheme.colorScheme.onSurface.copy(0.75f))
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "About this course",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontFamily = Montserrat,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(90.dp))
        }
    }
}
