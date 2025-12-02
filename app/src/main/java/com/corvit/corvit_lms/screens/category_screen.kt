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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController, authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){

    val authState = authViewModel.authState.observeAsState()
    val categories by catalogViewModel.categorylist.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
            }
            else -> {
                // Handle other states if needed
            }
        }
    }

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                   // .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No categories found")
            }
        } else {
            LazyColumn(
                //contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                CategoryCard(
                    name = category.name,
                    onClick = {
                        navController.navigate("course/${category.category_id}")
                    }
                )
            }

            }
        }

}


@Composable
fun CategoryCard(name: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }   // ← IMPORTANT
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
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Text(
            text = name,
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Montserrat,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
