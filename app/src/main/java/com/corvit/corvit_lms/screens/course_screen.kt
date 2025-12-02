package com.corvit.corvit_lms.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@Composable
fun CoursesScreen(
    navController: NavController,
    catalogViewModel: CatalogViewModel,
    categoryId: String
) {
    // Filter courses from your viewmodel
    val allCourses by catalogViewModel.courseslist.collectAsStateWithLifecycle()
    val courses = allCourses.filter { it.category_id == categoryId }

    if (courses.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No courses found")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(courses) { course ->
                CourseCard(name = course.name, certification = course.certification)
            }
        }
    }
}


@Composable
fun CourseCard(name : String, certification : Boolean){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
    ){
        Image(
            painter = painterResource(id = R.drawable.demo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column {
            Text(
                text = name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 35.sp,
                lineHeight = 42.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Text(
                text = if (certification) "Certified ✓" else "No Certificate",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 42.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

    }

}