package com.corvit.corvit_lms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.CorvitSuccessGreen
import com.corvit.corvit_lms.ui.theme.CorvitErrorRed
import com.corvit.corvit_lms.ui.theme.Montserrat

data class MockAttendanceSession(
    val date: String,
    val isPresent: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController) {
    val overallPercentage = 85
    val sessions = listOf(
        MockAttendanceSession("Feb 20, 2026", true),
        MockAttendanceSession("Feb 18, 2026", true),
        MockAttendanceSession("Feb 15, 2026", false),
        MockAttendanceSession("Feb 13, 2026", true)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Attendance", fontFamily = Montserrat, fontWeight = FontWeight.Bold) },
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
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            
            // Overall Stats Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = CorvitPrimaryRed),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Overall Attendance",
                        fontFamily = Montserrat,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$overallPercentage%",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Great job! Keep it up.",
                        fontFamily = Montserrat,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            Text(
                text = "Recent History",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sessions) { session ->
                    AttendanceRow(session)
                }
            }
        }
    }
}

@Composable
fun AttendanceRow(session: MockAttendanceSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = session.date,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            val statusColor = if (session.isPresent) CorvitSuccessGreen else CorvitErrorRed
            val statusText = if (session.isPresent) "Present" else "Absent"
            val bgColor = if (session.isPresent) CorvitSuccessGreen.copy(0.1f) else CorvitErrorRed.copy(0.1f)

            Text(
                text = statusText,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = statusColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}
