package com.corvit.corvit_lms.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.corvit.corvit_lms.others.YouTubePlayer
import com.corvit.corvit_lms.screens.components.CustomBottomBar
import com.corvit.corvit_lms.ui.theme.Montserrat
import com.corvit.corvit_lms.viewmodel.AuthState
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel, catalogViewModel : CatalogViewModel){

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val categories by catalogViewModel.categorylist.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
            }
            else -> {
                // Handle other states if needed
            }
        }
    }

    YouTubePlayer(videoId = "TqFdVoRTyzo", lifecycleOwner = LocalLifecycleOwner.current)

//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(title = {
//                Text(text = "Corvit",
//                    fontSize = 28.sp,
//                    style = TextStyle(
//                        fontFamily = Montserrat,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF000000)
//                    ))
//            })
//        },
//        bottomBar = { CustomBottomBar(navController) }
//    ) { innerPadding ->
//        if (categories.isEmpty()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("No categories found")
//            }
//        } else {
//            LazyColumn(
//                contentPadding = innerPadding,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items(categories) { category ->
//                    CategoryCard(name = category.name)
//                }
//            }
//        }
//    }


//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ){
//
//        Column {
//
//
//
//
////            Text("HomePage...",fontFamily = Montserrat,
////                fontWeight = FontWeight.Bold,
////                color = Color.Black,
////                fontSize = 40.sp)
//            if (categories.isEmpty()) {
//                Text("No categories found")
//            } else {
//                LazyColumn {
//                    items(categories) { category ->
//                        Text(
//                            text = category.name,
//                            fontFamily = Montserrat,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 20.sp,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(22.dp))
//
//            Button(
//                onClick = {
//                    authViewModel.logout()
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFBB2233),
//                    contentColor = Color(0xFFfbffe5)
//                ),
//                shape = RoundedCornerShape(100.dp),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(60.dp)
//            ) {
//                Text("Logout", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
//            }
//        }
//    }

}