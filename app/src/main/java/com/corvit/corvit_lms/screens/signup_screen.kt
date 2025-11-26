package com.corvit.corvit_lms.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel

@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel) {

    var name by remember { mutableStateOf("") }
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
                    .height(480.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFe9ecef))
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text("Sign Up",fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 40.sp)

                    Spacer(modifier = Modifier.height(22.dp))

                    // NAME FIELD
                    LabelledTextField(
                        label = "Name",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Enter your name"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    Spacer(modifier = Modifier.height(24.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = {
                            authViewModel.Signup(email, password)
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
                        Text("Sign Up", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate("login")
                },

                ) {
                Text(text = "Already have an account? Login",
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