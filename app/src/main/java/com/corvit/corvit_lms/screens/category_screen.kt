package com.corvit.corvit_lms.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.corvit.corvit_lms.screens.components.getCategoryImageResId
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import androidx.annotation.DrawableRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController, authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){

    val authState = authViewModel.authState.observeAsState()
    val categories by catalogViewModel.categorylist.collectAsStateWithLifecycle()
    val courses by catalogViewModel.courseslist.collectAsStateWithLifecycle()

    val sortedCategories = categories.sortedBy { it.order }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
            }
            else -> {
            }
        }
    }

    if (sortedCategories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Fix: Use theme content color
            Text("No categories found", color = MaterialTheme.colorScheme.onBackground)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(sortedCategories) { category ->

                val courseCount = courses.count { it.category_id == category.category_id }

                val imageResId = getCategoryImageResId(category.order)

                CategoryCard(
                    name = category.name,
                    courseCount = courseCount,
                    imageResId = imageResId,
                    onClick = {
                        navController.navigate("course/${category.category_id}")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    name: String,
    courseCount: Int,
    @DrawableRes imageResId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
        // Card uses the default surface color scheme which is theme-aware
    ) {
        Box(modifier = Modifier.height(140.dp)) {

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "$name thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay (kept black for maximum readability over image)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                // Maintain strong contrast with 0.7f black alpha
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0.3f
                        )
                    )
            )

            // Metadata badge (Course Count)
            Text(
                text = "$courseCount Courses",
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    // Use a slightly more theme-aware dark transparent color
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            // Category name
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}