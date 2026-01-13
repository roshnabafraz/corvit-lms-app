package com.corvit.corvit_lms.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
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
import androidx.navigation.NavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.ui.theme.CorvitPrimaryRed
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

private fun getGoogleClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, gso)
}

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val contentColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    // ✅ Firebase + Google client
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val googleClient = remember { getGoogleClient(context) }

    // ✅ Google Sign-In launcher
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            Toast.makeText(context, "Google sign-in cancelled", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Missing ID Token (default_web_client_id / SHA-1 issue)",
                    Toast.LENGTH_LONG
                ).show()
                return@rememberLauncherForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    Toast.makeText(context, "Google Login Success ✅", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message ?: "Google login failed", Toast.LENGTH_LONG).show()
                }

        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home")
            }
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                // ignore
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Corvit Logo",
                modifier = Modifier
                    .padding(top = 32.dp)
                    .height(200.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp) // 🔥 thora increase (google button ke liye)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        "Login",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = 40.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

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
                                modifier = Modifier.clickable { isChecked = !isChecked }
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

                    // ✅ EMAIL/PASSWORD LOGIN
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // ✅ GOOGLE SIGN-IN BUTTON
                    OutlinedButton(
                        onClick = {
                            googleLauncher.launch(googleClient.signInIntent)
                        },
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = contentColor
                        )
                    ) {
                        Text(
                            "Continue with Google",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(
                    text = "Don't have an account? SignUp",
                    color = contentColor,
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal
                )
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
            text = label,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
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
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            RoundedCornerShape(30.dp)
                        )
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
