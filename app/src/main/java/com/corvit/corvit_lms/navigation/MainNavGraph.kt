package com.corvit.corvit_lms.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.corvit.corvit_lms.screens.HomeScreen
import com.corvit.corvit_lms.screens.LoginScreen
import com.corvit.corvit_lms.screens.SignupScreen
import com.corvit.corvit_lms.screens.SplashScreen
import com.corvit.corvit_lms.viewmodel.AuthViewModel

@Composable
fun MainNavGraph(authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash", builder= {

        composable("splash") {
            SplashScreen {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("login"){
            LoginScreen( navController, authViewModel )
        }

        composable("signup"){
            SignupScreen( navController, authViewModel )
        }

        composable("home"){
            HomeScreen( navController, authViewModel )
        }
    })
}