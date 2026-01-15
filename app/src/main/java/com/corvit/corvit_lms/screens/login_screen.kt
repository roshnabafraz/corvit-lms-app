package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import androidx.navigation.NavController
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed // Import custom red

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val contentColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home")
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Ignore other states
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.corvit_logo),
                contentDescription = "Corvit Logo",
                modifier = Modifier
                    .padding(top = 32.dp)
                    .height(200.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text("Login",fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = 40.sp)

                    Spacer(modifier = Modifier.height(22.dp))

                    // INPUT FIELDS
                    LabelledTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter your email"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabelledTextField(
                        label = "Password",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Enter your password"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    var isChecked by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(
                                    id = if (isChecked) R.drawable.checkbox_bold else R.drawable.checkbox
                                ),
                                contentDescription = "Remember Me",
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { isChecked = !isChecked },
                                tint = contentColor
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Remember Me",
                                color = contentColor,
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.clickable{ isChecked = !isChecked }
                            )
                        }

                        TextButton(onClick = {}) {
                            Text(
                                text = "Forgot Password?",
                                color = contentColor,
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = { authViewModel.Login(email, password) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CorvitPrimaryRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                    ) {
                        Text("Login", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("signup") }
            ) {
                Text(text = "Don't have an account? SignUp",
                    color = contentColor,
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal)
            }
        }
    }
}

@Composable
fun LabelledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth(0.9f)) {

        Text(
            text = label,fontFamily = Montserrat, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontFamily = Montserrat
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            RoundedCornerShape(30.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = Montserrat
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}