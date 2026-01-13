package com.corvit.corvit_lms.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Enroll_Screen(
    navController: NavController,
    courseId: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enrolled Successfully!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(6.dp))
            Text("Course ID: $courseId")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) { Text("Back") }
        }
    }
}
