package com.corvit.corvit_lms.screens.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.corvit.corvit_lms.R

@Composable
fun CustomBottomBar(navController: NavController) {

    val selectedItem = remember { mutableStateOf("home") }
    val iconSize = 35.dp

    val items = listOf(
        BottomBarItem("home", R.drawable.home_linear, R.drawable.home_bold),
        BottomBarItem("categories", R.drawable.grad_linear, R.drawable.grad_bold),
        BottomBarItem("notifications", R.drawable.bell_linear, R.drawable.bell_bold),
        BottomBarItem("settings", R.drawable.style_linear, R.drawable.style_bold)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(item.route)
                        selectedItem.value = item.route
                    }
                ) {
                    Icon(
                        painter = if (selectedItem.value == item.route) painterResource(id = item.iconBold)
                        else painterResource(id = item.iconLinear),
                        contentDescription = item.route,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

data class BottomBarItem(
    val route: String,
    val iconLinear: Int,
    val iconBold: Int
)
