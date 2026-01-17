package com.corvit.corvit_lms.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.screens.CategoryScreen
import com.corvit.corvit_lms.screens.CourseDetailScreen
import com.corvit.corvit_lms.screens.CoursesScreen
import com.corvit.corvit_lms.screens.EnrollDoneScreen
import com.corvit.corvit_lms.screens.Enroll_Screen
import com.corvit.corvit_lms.screens.HomeScreen
import com.corvit.corvit_lms.screens.LoginScreen
import com.corvit.corvit_lms.screens.NotificationScreen
import com.corvit.corvit_lms.screens.ProfileScreen
import com.corvit.corvit_lms.screens.SignupScreen
import com.corvit.corvit_lms.screens.SplashScreen
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.screens.components.LocalThemeToggleState
import com.corvit.corvit_lms.screens.components.ThemeToggleState
import com.corvit.corvit_lms.ui.theme.CorvitLMSTheme
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(authViewModel: AuthViewModel, catalogViewModel: CatalogViewModel, userName: String) {
    val navController = rememberNavController()

    // 1. Observe the current back stack entry to get the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define Bar Visibility

    // Hide Top Bar on these screens (Includes 'home' as it usually has its own header)
    val noTopBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "course_detail/{courseId}"
    )

    // Hide Bottom Bar on these screens
    val noBottomBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "course_detail/{courseId}"
    )

    val showTopBar = currentRoute !in noTopBarScreens
    val showBottomBar = currentRoute !in noBottomBarScreens

    // 3. Theme State
    val isDarkThemeEnabled = rememberSaveable { mutableStateOf(false) }

    val themeState = remember(isDarkThemeEnabled.value) {
        ThemeToggleState(
            isDarkTheme = isDarkThemeEnabled.value,
            toggleTheme = { isDarkThemeEnabled.value = it }
        )
    }

    CompositionLocalProvider(LocalThemeToggleState provides themeState) {
        CorvitLMSTheme(darkTheme = isDarkThemeEnabled.value) {

            Scaffold(
                topBar = {
                    if (showTopBar) {
                        CenterAlignedTopAppBar(title = {
//                            Text(
//                                text = "Corvit",
//                                fontSize = 28.sp,
//                                style = TextStyle(
//                                    fontFamily = Montserrat,
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color(0xFF000000)
//                                )
//                            )
                            Image(
                                // Make sure 'logo' exists in your res/drawable folder
                                painter = painterResource(id = R.drawable.corvit_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .height(40.dp) // Adjust height to fit the bar
                                    .width(120.dp), // Optional: Limit width if needed
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        })
                    }
                },
                bottomBar = {
                    if (showBottomBar) {
                        CustomBottomBar(navController)
                    }
                }
            ) { innerPadding ->

                NavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    startDestination = "splash"
                ) {

                    // --- SPLASH ---
                    composable("splash") {
                        SplashScreen {
                            // Navigate to Login as requested
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }

                    // --- AUTH ---
                    composable("login") {
                        LoginScreen(navController, authViewModel)
                    }

                    composable("signup") {
                        SignupScreen(navController, authViewModel)
                    }

                    // --- MAIN TABS ---
                    composable("home") {
                        // Original HomeScreen enabled
                        HomeScreen(navController, authViewModel, catalogViewModel)
                    }

                    composable("categories") {
                        CategoryScreen(navController, authViewModel, catalogViewModel)
                    }

                    composable("notifications") {
                        NotificationScreen()
                    }

                    composable("profile") {
                        ProfileScreen(navController, authViewModel, userName = userName )
                    }

                    // --- COURSES & ENROLLMENT ---
                    composable("course/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                        CoursesScreen(navController, catalogViewModel, categoryId)
                    }

                    composable("course_detail/{courseId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        CourseDetailScreen(navController, catalogViewModel, courseId)
                    }

                    composable("enroll/{courseId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        Enroll_Screen(navController = navController, courseId = courseId)
                    }

                    composable(
                        route = "enroll_demo/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("courseId") ?: ""
                        Enroll_Screen(navController = navController, courseId = id)
                    }

                    composable("enroll_done/{courseId}/{name}/{phone}/{city}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val phone = backStackEntry.arguments?.getString("phone") ?: ""
                        val city = backStackEntry.arguments?.getString("city") ?: ""
                        EnrollDoneScreen(navController, courseId, name, phone, city)
                    }
                }
            }
        }
    }
}