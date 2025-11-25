package com.corvit.corvit_lms.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SplashScreen(){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Text(text = "Splash Screen")
        }
    }
}