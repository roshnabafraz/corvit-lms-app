package com.corvit.corvit_lms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(navController: NavController) {

    // 1. Define the Cities and their specific URLs
    val cityTabs = listOf("Lahore", "Rawalpindi", "Islamabad")

    // NOTE: Update these URLs monthly when the schedule changes!
    val cityUrls = mapOf(
        "Lahore" to "https://corvit.com/systems/wp-content/uploads/2026/01/Lahore-Jan-26-1.png",
        "Rawalpindi" to "https://corvit.com/systems/wp-content/uploads/2025/08/Pindi-Aug-2025.png",
        "Islamabad" to "https://corvit.com/systems/wp-content/uploads/2025/08/Isb-Aug-2025.png"
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val currentUrl = cityUrls[cityTabs[selectedTabIndex]] ?: ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Class Schedule",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // --- TAB ROW (City Selector) ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = CorvitPrimaryRed,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = CorvitPrimaryRed
                    )
                }
            ) {
                cityTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontFamily = Montserrat,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        unselectedContentColor = Color.Gray
                    )
                }
            }

            // --- IMAGE DISPLAY ---
            // We use Box + VerticalScroll to handle long images
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp), // Add space between tabs and image
                contentAlignment = Alignment.TopCenter
            ) {
                // We use 'key(currentUrl)' to force the image to refresh when the tab changes
                key(currentUrl) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${cityTabs[selectedTabIndex]} Schedule",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = CorvitPrimaryRed)
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Failed to load schedule.", fontFamily = Montserrat)
                            }
                        }
                    )
                }

                // Add some bottom padding for better scrolling experience
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}