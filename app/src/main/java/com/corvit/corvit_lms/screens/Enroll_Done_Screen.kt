package com.corvit.corvit_lms.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EnrollDoneScreen(
    navController: NavController,
    courseId: String,
    name: String,
    phone: String,
    city: String
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enrolled Successfully!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(10.dp))
            Text("Course: $courseId")
            Text("Name: $name")
            Text("Phone: $phone")
            Text("City: $city")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) { Text("Back") }
        }
    }
}
