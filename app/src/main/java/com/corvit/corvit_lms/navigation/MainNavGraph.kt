package com.corvit.corvit_lms.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.corvit.corvit_lms.screens.CategoryScreen
import com.corvit.corvit_lms.screens.HomeScreen
import com.corvit.corvit_lms.screens.LoginScreen
import com.corvit.corvit_lms.screens.SignupScreen
import com.corvit.corvit_lms.screens.SplashScreen
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Corvit",
                    fontSize = 28.sp,
                    style = TextStyle(
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                )
            })
        },
        bottomBar = { CustomBottomBar(navController) }
    ) { innerPadding ->

        NavHost(navController = navController,
            modifier = Modifier.padding(innerPadding)
            , startDestination = "splash",
            builder= {

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
                HomeScreen( navController, authViewModel, catalogViewModel)
            }

            composable("categories"){
                CategoryScreen( navController, authViewModel, catalogViewModel)
            }

        })

    }
}