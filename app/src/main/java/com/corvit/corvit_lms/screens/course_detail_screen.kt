package com.corvit.corvit_lms.screens

import android.widget.Toast
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

@Composable
fun CourseDetailScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    courseName: String
) {
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()
    val course = allCourses.firstOrNull { it.name == courseName }

    if (course == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found", fontFamily = Montserrat)
        }
        return
    }

    val regularPrice = course.prices?.discount_pkr ?: 0.0
    val discountPrice = course.prices?.regular_pkr ?: 0.0

    val finalPrice = if (discountPrice > 0 && discountPrice < regularPrice) discountPrice else regularPrice

    val description = remember {
        """
        ${course.name} is designed to help you build strong, practical skills with a clear learning path. You’ll learn core concepts with real-world examples and industry-style practices.

        This course includes structured learning modules, guided practice, and key takeaways that help you apply knowledge in projects and assessments. You’ll also get a clear understanding of best practices used by professionals.
        """.trimIndent()
    }

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
                            text = if (discountPrice > 0 && discountPrice < regularPrice) "Discounted Price" else "Total Price",
                            fontFamily = Montserrat,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = if (finalPrice == 0.0) "Free" else "Rs. ${String.format("%,.0f", finalPrice)}",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = CorvitPrimaryRed
                        )
                    }

                    Button(
                        onClick = {
                            Toast.makeText(navController.context, "Enrolled in ${course.name}", Toast.LENGTH_SHORT).show()
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

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = course.vendor ?: "Corvit Systems",
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
                    DetailChip(Icons.Default.Star, course.courseLevel ?: "Beginner")
                    DetailChip(Icons.Default.DateRange, course.duration ?: "N/A")
                    DetailChip(Icons.Default.Person, course.batchType ?: "Physical")
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
                    DetailItem("Certification", if (course.certification) "Yes" else "No", course.certification)
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.Gray.copy(0.3f)))
                    DetailItem("Language", "English/Urdu", false)
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.Gray.copy(0.3f)))
                    DetailItem("Vendor", course.vendor ?: "Corvit", false)
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

                Spacer(modifier = Modifier.height(24.dp))

                // 3. FEE STRUCTURE
                Text(
                    text = "Fee Structure",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (regularPrice > 0) {
                            PriceRowItem("Regular Fee", "Rs. ${String.format("%,.0f", regularPrice)}")
                        }

                        if (discountPrice > 0 && discountPrice < regularPrice) {
                            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                            PriceRowItem("Discounted Fee", "Rs. ${String.format("%,.0f", discountPrice)}", isHighlight = true)
                        }

                        course.prices?.group_usd?.let { usd ->
                            if (usd > 0) {
                                HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                                PriceRowItem("Group Training (USD)", "$ ${String.format("%,.0f", usd)}")
                            }
                        }

                        course.prices?.one_to_one_usd?.let { usd ->
                            if (usd > 0) {
                                HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                                PriceRowItem("1-on-1 Training (USD)", "$ ${String.format("%,.0f", usd)}")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun PriceRowItem(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (isHighlight) Color(0xFF4CAF50) else CorvitPrimaryRed
        )
    }
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