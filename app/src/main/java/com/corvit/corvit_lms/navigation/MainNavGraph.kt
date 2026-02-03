package com.corvit.corvit_lms.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.corvit.corvit_lms.R
import com.corvit.corvit_lms.screens.CourseDetailScreen
import com.corvit.corvit_lms.screens.CoursesScreen
import com.corvit.corvit_lms.screens.Enroll_Screen
import com.corvit.corvit_lms.screens.HomeScreen
import com.corvit.corvit_lms.screens.LoginScreen
import com.corvit.corvit_lms.screens.NotificationScreen
import com.corvit.corvit_lms.screens.ProfileScreen
import com.corvit.corvit_lms.screens.SignupScreen
import com.corvit.corvit_lms.screens.SplashScreen
import com.corvit.corvit_lms.screens.TimetableScreen
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.screens.components.LocalThemeToggleState
import com.corvit.corvit_lms.screens.components.ThemeToggleState
import com.corvit.corvit_lms.ui.theme.CorvitLMSTheme
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.corvit.corvit_lms.screens.FAQScreen
import com.corvit.corvit_lms.screens.EnrollDoneScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(authViewModel: AuthViewModel, catalogViewModel: CatalogViewModel, userName: String) {
    val navController = rememberNavController()

    // 1. Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define Bar Visibility
    val noTopBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "course_detail/{courseName}/{courseId}",
        "enrollment/{courseName}/{courseId}",
        "enroll_done/{courseName}/{studentName}/{batchName}/{city}"
    )

    val noBottomBarScreens = listOf(
        "splash",
        "login",
        "signup",
        "course_detail/{courseName}/{courseId}",
        "enrollment/{courseName}/{courseId}",
        "enroll_done/{courseName}/{studentName}/{batchName}/{city}"
    )

    val showTopBar = currentRoute !in noTopBarScreens
    val showBottomBar = currentRoute !in noBottomBarScreens

    // 3. Theme State
    val systemTheme = isSystemInDarkTheme()
    val isDarkThemeEnabled = rememberSaveable { mutableStateOf(systemTheme) }

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
                            Image(
                                painter = painterResource(id = R.drawable.corvit_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(120.dp),
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
                    startDestination = "splash",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) }
                ) {

                    // --- SPLASH & AUTH ---
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("login") { popUpTo("splash") { inclusive = true } }
                        }
                    }
                    composable("login") { LoginScreen(navController, authViewModel) }
                    composable("signup") { SignupScreen(navController, authViewModel) }

                    // --- MAIN TABS ---
                    composable("home") { HomeScreen(navController, authViewModel, catalogViewModel) }

                    composable("timetable") { TimetableScreen(navController = navController) }

                    composable("notifications") { NotificationScreen() }

                    composable("profile") { ProfileScreen(navController, authViewModel, userName = userName) }

                    composable("faq") { FAQScreen(navController) }

                    // --- COURSES ---
                    // "categories" tab now points directly to the list of courses
                    composable("categories") {
                        CoursesScreen(navController = navController, catalogViewModel = catalogViewModel)
                    }

                    // Course Details (Now accepts ID)
                    composable(
                        route = "course_detail/{courseName}/{courseId}",
                        arguments = listOf(
                            navArgument("courseName") { type = NavType.StringType },
                            navArgument("courseId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
                        val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
                        CourseDetailScreen(navController, catalogViewModel, courseName, courseId)
                    }

                    // --- ENROLLMENT ---
                    composable(
                        route = "enrollment/{courseName}/{courseId}",
                        arguments = listOf(
                            navArgument("courseName") { type = NavType.StringType },
                            navArgument("courseId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
                        val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

                        Enroll_Screen(
                            navController = navController,
                            authViewModel = authViewModel,
                            courseName = courseName,
                            courseId = courseId
                        )
                    }

                    // Success Screen
                    composable(
                        route = "enroll_done/{courseName}/{studentName}/{batchName}/{city}",
                        arguments = listOf(
                            navArgument("courseName") { type = NavType.StringType },
                            navArgument("studentName") { type = NavType.StringType },
                            navArgument("batchName") { type = NavType.StringType },
                            navArgument("city") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        EnrollDoneScreen(
                            navController = navController,
                            courseId = backStackEntry.arguments?.getString("courseName") ?: "",
                            name = backStackEntry.arguments?.getString("studentName") ?: "",
                            phone = backStackEntry.arguments?.getString("batchName") ?: "", // passing batch as phone param for display
                            city = backStackEntry.arguments?.getString("city") ?: ""
                        )
                    }
                }
            }
        }
    }
}