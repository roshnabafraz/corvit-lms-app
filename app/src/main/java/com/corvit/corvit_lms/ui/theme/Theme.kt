package com.corvit.corvit_lms.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// --- Light Theme Colors ---
private val LightColorScheme = lightColorScheme(
    primary = CorvitPrimaryRed,    // Main brand color
    onPrimary = Color.White,       // Text on primary

    // Background and content colors
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),

    // Card and main surface colors
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),

    // Custom surface for login/signup boxes and section cards (light gray)
    surfaceVariant = Color(0xFFF6F6F6),
    onSurfaceVariant = Color(0xFF1C1B1F)
)

// --- Dark Theme Colors ---
private val DarkColorScheme = darkColorScheme(
    primary = CorvitPrimaryRed,
    onPrimary = Color.White,

    // Background and content colors
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFFFFBFE),

    // Card and main surface colors
    surface = Color(0xFF2C2C2C),
    onSurface = Color(0xFFFFFBFE),

    // Custom dark surface for login/signup boxes and section cards (a slightly lighter dark gray)
    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = Color.White
)

@Composable
fun CorvitLMSTheme(
    // The key parameter controlled by the ThemeToggleSwitch
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Use dynamic colors if available
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography is defined elsewhere
        content = content
    )
}