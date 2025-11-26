package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home")
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Handle other states if needed
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Corvit Logo",
                modifier = Modifier
                    .padding(top = 32.dp)
                    .height(200.dp)
            )

            //Spacer(modifier = Modifier.height(32.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFe9ecef))
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text("Login",fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 40.sp)

                    Spacer(modifier = Modifier.height(22.dp))

                    // EMAIL FIELD
                    LabelledTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter your email"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // PASSWORD FIELD
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

                        // Left side: Checkbox + Text
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (isChecked) R.drawable.checkbox_bold else R.drawable.checkbox
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { isChecked = !isChecked },
                                tint = Color(0xFF001011)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Remember Me",
                                color = Color(0xFF001011),
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.clickable{
                                    isChecked = !isChecked
                                }
                            )
                        }

                        // Right side: Forgot Password
                        TextButton(onClick = {}) {
                            Text(
                                text = "Forgot Password?",
                                color = Color(0xFF001011),
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = {
                            authViewModel.Login(email, password)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBB2233),
                            contentColor = Color(0xFFfbffe5)
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
                onClick = {
                    navController.navigate("signup")
                },

                ) {
                Text(text = "Don't have an account? SignUp",
                    color = Color(0xFF001011),
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal)
            }

            //Spacer(modifier = Modifier.height(16.dp))

            //SIGN-UP BUTTON
//            Button(
//                onClick = {
//                    navController.navigate("signup")
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF001011),
//                    contentColor = Color(0xFFfbffe5)
//                ),
//                shape = RoundedCornerShape(20.dp),
//                modifier = Modifier
//                    .fillMaxWidth(0.9f)
//                    .height(70.dp)
//            ) {
//                Text("Sign Up")
//            }
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
            color = Color(0xFF001011),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Montserrat
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF001011),
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
