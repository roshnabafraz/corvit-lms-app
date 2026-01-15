package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Enroll_Screen(
    navController: NavController,
    courseId: String
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // error states
    var nameErr by remember { mutableStateOf<String?>(null) }
    var phoneErr by remember { mutableStateOf<String?>(null) }
    var cityErr by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        val n = fullName.trim()
        val p = phone.trim()
        val c = city.trim()

        nameErr = if (n.length < 3) "Full name required (min 3 chars)" else null
        phoneErr = if (p.length < 10) "Valid phone required (min 10 digits)" else null
        cityErr = if (c.isEmpty()) "City required" else null

        return nameErr == null && phoneErr == null && cityErr == null
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Enroll Form") }
            )

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Course ID: $courseId", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it; nameErr = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full Name") },
                isError = nameErr != null,
                supportingText = { if (nameErr != null) Text(nameErr!!) }
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; phoneErr = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneErr != null,
                supportingText = { if (phoneErr != null) Text(phoneErr!!) }
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it; cityErr = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("City") },
                isError = cityErr != null,
                supportingText = { if (cityErr != null) Text(cityErr!!) }
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email (optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (!validate()) return@Button

                    // demo success message
                    Toast.makeText(context, "Enrolled successfully", Toast.LENGTH_SHORT).show()

                    // later: here we will send data to Google Sheet

                    // demo success screen open with data (optional)
                    val encName = URLEncoder.encode(fullName.trim(), StandardCharsets.UTF_8.toString())
                    val encPhone = URLEncoder.encode(phone.trim(), StandardCharsets.UTF_8.toString())
                    val encCity = URLEncoder.encode(city.trim(), StandardCharsets.UTF_8.toString())

                    navController.navigate("enroll_done/$courseId/$encName/$encPhone/$encCity")
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("ENROLL")
            }
        }
    }
}
