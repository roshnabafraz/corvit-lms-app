package com.corvit.corvit_lms.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corvit.corvit_lms.ui.theme.Montserrat

@Preview(showSystemUi = true)
@Composable
fun CourseDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        // Course Title
        AppText(
            text = "Android Development with Jetpack Compose",
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Course Level + Duration Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            InfoChip(text = "Beginner to Advanced")
            Spacer(modifier = Modifier.width(8.dp))
            InfoChip(text = "12 Weeks")
            Spacer(modifier = Modifier.width(8.dp))
            InfoChip(text = "Hands-on")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText(
                    text = "About this course",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = "Learn modern Android app development using Jetpack Compose. This course focuses on real-world projects, clean architecture, and industry best practices.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Course Details
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailRow(label = "Instructor", value = "Certified Android Engineer")
                DetailRow(label = "Total Lectures", value = "48")
                DetailRow(label = "Daily Time", value = "1.5 Hours")
                DetailRow(label = "Course Fee", value = "PKR 35,000")
                DetailRow(label = "Certificate", value = "Yes")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Enroll Button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            shape = RoundedCornerShape(12.dp)
        ) {
            AppText(
                text = "Enroll Now",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    color: Color = Color.Black,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEDEDED), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        AppText(
            text = text,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        AppText(
            text = value,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
