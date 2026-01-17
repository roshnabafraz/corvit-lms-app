package com.corvit.corvit_lms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import com.corvit.corvit_lms.navigation.MainNavGraph
import com.corvit.corvit_lms.ui.theme.CorvitLMSTheme
import com.corvit.corvit_lms.viewmodel.AuthViewModel
import com.corvit.corvit_lms.viewmodel.CatalogViewModel
import com.corvit.corvit_lms.viewmodel.UserDataState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()
        val catalogViewModel : CatalogViewModel  by viewModels()
        setContent {
            CorvitLMSTheme {

                LaunchedEffect(Unit) {
                    authViewModel.getUserName()
                }

                val userDataState by authViewModel.userDataState.observeAsState()

                val globalUserName = remember(userDataState) {
                    when (val state = userDataState) {
                        is UserDataState.Success -> state.name
                        is UserDataState.Loading -> "Loading..."
                        is UserDataState.Error -> "Student"
                        else -> "Guest"
                    }
                }

                MainNavGraph(authViewModel, catalogViewModel, globalUserName)
            }
        }
    }
}