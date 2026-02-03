package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.data.ApiBatch
import com.corvit.corvit_lms.data.EnrollmentRequest
import com.corvit.corvit_lms.network.RetrofitClient
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Enroll_Screen(
    navController: NavController,
    authViewModel: AuthViewModel,
    courseName: String,
    courseId: Int
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    var batches by remember { mutableStateOf<List<ApiBatch>>(emptyList()) }
    var selectedBatch by remember { mutableStateOf<ApiBatch?>(null) }
    var isBatchExpanded by remember { mutableStateOf(false) }
    var isLoadingBatches by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.getUserData()

        try {
            val response = RetrofitClient.api.getBatches(courseId)
            if (response.isSuccessful && response.body()?.status == true) {
                batches = response.body()!!.batches
            }
        } catch (e: Exception) {
        } finally {
            isLoadingBatches = false
        }
    }

    val userDataState = authViewModel.userDataState.observeAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Enroll in Course", fontFamily = Montserrat, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(text = "Applying for:", color = Color.Gray, fontSize = 14.sp, fontFamily = Montserrat)
            Text(text = courseName, color = CorvitPrimaryRed, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = Montserrat)
            HorizontalDivider(color = Color.Gray.copy(0.2f))

            LabelledOutlinedTextField("Full Name", fullName, { fullName = it }, "Enter Name")
            LabelledOutlinedTextField("Phone", phone, { phone = it }, "03XX-XXXXXXX", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            Text("Select Batch", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp)

            ExposedDropdownMenuBox(
                expanded = isBatchExpanded,
                onExpandedChange = { isBatchExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedBatch?.formatForDropdown()
                        ?: if (isLoadingBatches) "Loading schedule..."
                        else if (batches.isEmpty()) "No upcoming batches"
                        else "Select a batch",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    enabled = !isLoadingBatches && batches.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = isBatchExpanded,
                    onDismissRequest = { isBatchExpanded = false }
                ) {
                    batches.forEach { batch ->
                        DropdownMenuItem(
                            text = { Text(batch.formatForDropdown(), fontFamily = Montserrat) },
                            onClick = {
                                selectedBatch = batch
                                isBatchExpanded = false
                            }
                        )
                    }
                }
            }

            LabelledOutlinedTextField("Remarks (Optional)", remarks, { remarks = it }, "Any questions?")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (fullName.isNotEmpty() && phone.isNotEmpty()) {
                        isSubmitting = true
                        coroutineScope.launch {
                            try {
                                val request = EnrollmentRequest(
                                    studentName = fullName,
                                    phone = phone,
                                    courseId = courseId,
                                    batchId = selectedBatch?.id,
                                    remarks = remarks
                                )

                                val response = RetrofitClient.api.submitEnrollment(request)

                                if (response.isSuccessful && response.body()?.status == true) {
                                    // Success
                                    navController.navigate("enroll_done/$courseName/$fullName/${selectedBatch?.name ?: "Pending"}/Lahore")
                                } else {
                                    Toast.makeText(context, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
                            } finally {
                                isSubmitting = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CorvitPrimaryRed),
                enabled = !isSubmitting,
                shape = RoundedCornerShape(100.dp)
            ) {
                if (isSubmitting) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                else Text("Submit Application", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LabelledOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        Text(text = label, fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontFamily = Montserrat, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions,
            singleLine = true
        )
    }
}