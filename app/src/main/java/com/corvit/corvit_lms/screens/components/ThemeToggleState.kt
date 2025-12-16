package com.corvit.corvit_lms.screens.components

import androidx.compose.runtime.compositionLocalOf

data class ThemeToggleState(
    val isDarkTheme: Boolean,
    val toggleTheme: (Boolean) -> Unit
)

val LocalThemeToggleState = compositionLocalOf<ThemeToggleState> {
    error("ThemeToggleState not provided")
}