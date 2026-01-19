package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Enroll_Screen(
    navController: NavController,
    authViewModel: AuthViewModel,
    courseName: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- 1. Auto-Detect Email ---
    val userDataState = authViewModel.userDataState.observeAsState()

    // Trigger data fetch if needed
    LaunchedEffect(Unit) { authViewModel.getUserData() }

    val autoEmail = when(val state = userDataState.value) {
        is UserDataState.Success -> state.email
        else -> ""
    }

    // --- 2. Form State ---
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Dropdown State
    var isCityExpanded by remember { mutableStateOf(false) }
    val cities = listOf(
        "Lahore", "Karachi", "Islamabad", "Rawalpindi", "Faisalabad",
        "Multan", "Peshawar", "Quetta", "Sialkot", "Gujranwala",
        "Bahawalpur", "Sargodha", "Sukkur", "Larkana", "Sheikhupura",
        "Rahim Yar Khan", "Jhang", "Dera Ghazi Khan", "Gujrat", "Sahiwal",
        "Wah Cantonment", "Mardan", "Kasur", "Okara", "Mingora",
        "Nawabshah", "Chiniot", "Kotri", "Kāmoke", "Hafizabad"
    )

    // Error States
    var nameErr by remember { mutableStateOf<String?>(null) }
    var phoneErr by remember { mutableStateOf<String?>(null) }
    var cityErr by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // 🔥 PASTE YOUR GOOGLE SCRIPT URL HERE
    val scriptUrl = "https://script.google.com/macros/s/AKfycbwIGEJZGC_93dcLhqLw404GPzGowDtr9ogjZ8L-TRZat8PEcxSoDf-2SpsE4rWWvdX0VA/exec"

    // Validation Logic
    fun validate(): Boolean {
        val n = fullName.trim()
        val p = phone.trim()
        val c = city.trim()

        nameErr = if (n.length < 3) "Full name required (min 3 chars)" else null
        phoneErr = if (p.length < 10) "Valid phone required (min 10 digits)" else null
        cityErr = if (c.isEmpty()) "Please select a city" else null

        return nameErr == null && phoneErr == null && cityErr == null
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Enrollment Form",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header Section
            Column {
                Text(
                    text = "You are applying for:",
                    fontFamily = Montserrat,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = courseName,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CorvitPrimaryRed
                )
            }

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))

            // --- FORM FIELDS ---

            // 1. Full Name
            LabelledOutlinedTextField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it; nameErr = null },
                placeholder = "Enter your full name",
                isError = nameErr != null,
                errorMessage = nameErr,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            // 2. Phone Number
            LabelledOutlinedTextField(
                label = "WhatsApp / Phone",
                value = phone,
                onValueChange = { phone = it; phoneErr = null },
                placeholder = "03XX-XXXXXXX",
                isError = phoneErr != null,
                errorMessage = phoneErr,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // 3. Email (Read Only)
            LabelledOutlinedTextField(
                label = "Email Address",
                value = if (autoEmail.isNotEmpty()) autoEmail else "Loading...",
                onValueChange = {}, // Read-only
                placeholder = "Your email",
                enabled = false // Locked
            )

            // 4. City Dropdown
            Text(
                text = "City",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            ExposedDropdownMenuBox(
                expanded = isCityExpanded,
                onExpandedChange = { isCityExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select your city", fontFamily = Montserrat) },
                    trailingIcon = {
                        Icon(
                            imageVector = if (isCityExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CorvitPrimaryRed,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(), // Required for M3 dropdown
                    isError = cityErr != null
                )

                ExposedDropdownMenu(
                    expanded = isCityExpanded,
                    onDismissRequest = { isCityExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    cities.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, fontFamily = Montserrat) },
                            onClick = {
                                city = selectionOption
                                cityErr = null
                                isCityExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            if (cityErr != null) {
                Text(
                    text = cityErr!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- SUBMIT BUTTON ---
            Button(
                onClick = {
                    if (validate()) {
                        if (autoEmail.isEmpty()) {
                            Toast.makeText(context, "Email not loaded yet", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        coroutineScope.launch {
                            val success = sendEnrollmentToSheet(scriptUrl, fullName, phone, autoEmail, city, courseName)
                            isLoading = false

                            if (success) {
                                Toast.makeText(context, "Enrollment Request Sent!", Toast.LENGTH_LONG).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Failed. Check internet or try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fix errors", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CorvitPrimaryRed),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Enrollment", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

// --- HELPER COMPOSABLE FOR TEXT FIELDS ---
@Composable
fun LabelledOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    Column {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontFamily = Montserrat, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Montserrat),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CorvitPrimaryRed,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
            singleLine = true
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// --- NETWORK FUNCTION ---
suspend fun sendEnrollmentToSheet(
    scriptUrl: String,
    name: String,
    phone: String,
    email: String,
    city: String,
    course: String
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(scriptUrl)
            val postData = buildString {
                append("name=").append(URLEncoder.encode(name, "UTF-8"))
                append("&phone=").append(URLEncoder.encode(phone, "UTF-8"))
                append("&email=").append(URLEncoder.encode(email, "UTF-8"))
                append("&city=").append(URLEncoder.encode(city, "UTF-8"))
                append("&course=").append(URLEncoder.encode(course, "UTF-8"))
            }

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.outputStream.write(postData.toByteArray(Charsets.UTF_8))

            val responseCode = conn.responseCode
            responseCode == HttpURLConnection.HTTP_OK || responseCode == 200
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}