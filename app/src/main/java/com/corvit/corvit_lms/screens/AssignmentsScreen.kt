package com.corvit.corvit_lms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.CorvitSuccessGreen
import com.corvit.corvit_lms.ui.theme.Montserrat

data class MockAssignment(
    val title: String,
    val course: String,
    val dueDate: String,
    val isSubmitted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(navController: NavController) {
    val assignments = listOf(
        MockAssignment("Lab 4: VPC Peering", "AWS Solutions Architect", "Feb 28, 2026", false),
        MockAssignment("React Frontend Build", "Full Stack Web Dev", "Mar 05, 2026", false),
        MockAssignment("EC2 Instance Setup", "AWS Solutions Architect", "Feb 10, 2026", true)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Assignments", fontFamily = Montserrat, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(assignments) { assignment ->
                AssignmentCard(assignment)
            }
        }
    }
}

@Composable
fun AssignmentCard(assignment: MockAssignment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (assignment.isSubmitted) CorvitSuccessGreen.copy(0.1f) 
                        else CorvitPrimaryRed.copy(0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (assignment.isSubmitted) Icons.Default.CheckCircle else Icons.Default.Assignment,
                    contentDescription = null,
                    tint = if (assignment.isSubmitted) CorvitSuccessGreen else CorvitPrimaryRed
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = assignment.course,
                    fontFamily = Montserrat,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (assignment.isSubmitted) "Submitted" else "Due: ${assignment.dueDate}",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = if (assignment.isSubmitted) CorvitSuccessGreen else CorvitPrimaryRed
                    )
                }
            }
        }
    }
}
