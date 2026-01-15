package com.corvit.corvit_lms.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue // Import for 'by' delegate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState // Import this
import androidx.navigation.compose.rememberNavController
import com.corvit.corvit_lms.screens.CategoryScreen
import com.corvit.corvit_lms.screens.CourseDetailScreen
import com.corvit.corvit_lms.screens.CoursesScreen
import com.corvit.corvit_lms.screens.EnrollDoneScreen
import com.corvit.corvit_lms.screens.Enroll_Screen
import com.corvit.corvit_lms.screens.HomeScreen
import com.corvit.corvit_lms.screens.HomeScreen2
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
fun MainNavGraph(authViewModel: AuthViewModel, catalogViewModel: CatalogViewModel) {
    val navController = rememberNavController()

    // 1. Observe the current back stack entry to get the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define which screens should NOT show the bars
    // Screens where TopBar should NOT show
    val noTopBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "home2",
        "course_detail/{courseId}"
    )

// Screens where BottomBar should NOT show
    val noBottomBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "course_detail/{courseId}"
        // ✅ Notice: home2 is NOT here, so bottom bar will show on home2
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
                    // Only show TopBar if showBars is true
                    if (showTopBar) {
                        CenterAlignedTopAppBar(title = {
                            Text(
                                text = "Corvit",
                                fontSize = 28.sp,
                                style = TextStyle(
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Bold,
                                    // Note: You might want to switch this Color to use Theme colors eventually
                                    // so it adapts to Dark Mode automatically.
                                    color = Color(0xFF000000)
                                )
                            )
                        })
                    }
                },
                bottomBar = {
                    // Only show BottomBar if showBars is true
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
                    composable("enroll/{courseId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        Enroll_Screen(navController = navController, courseId = courseId)
                    }

                    composable("enroll_done/{courseId}/{name}/{phone}/{city}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val phone = backStackEntry.arguments?.getString("phone") ?: ""
                        val city = backStackEntry.arguments?.getString("city") ?: ""
                        EnrollDoneScreen(navController, courseId, name, phone, city)
                    }

                    composable("splash") {
                        SplashScreen {
                            navController.navigate("home2") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }

                    composable("login") {
                        LoginScreen(navController, authViewModel)
                    }

                    composable("signup") {
                        SignupScreen(navController, authViewModel)
                    }

                    composable("home") {
                        // HomeScreen(navController, authViewModel, catalogViewModel)
                        HomeScreen()
                    }
                    composable("home2") {
                        HomeScreen2(navController, authViewModel, catalogViewModel)
                    }

                    composable("course/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                        CoursesScreen(navController, catalogViewModel, categoryId)
                    }
                    composable("course_detail/{courseId}") { backStackEntry ->
                        val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                        CourseDetailScreen(navController, catalogViewModel, courseId)
                    }

                    composable("categories") {
                        CategoryScreen(navController, authViewModel, catalogViewModel)
                    }

                    composable("notifications") {
                        NotificationScreen()
                    }

                    composable("profile") {
                        ProfileScreen(navController, authViewModel)
                    }
                    composable(
                        route = "enroll_demo/{courseId}",
                        arguments = listOf(navArgument("courseId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("courseId") ?: ""
                        Enroll_Screen(navController = navController, courseId = id)
                    }

                }
            }
        }
    }
}