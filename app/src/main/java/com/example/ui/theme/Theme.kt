package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SonicRed,
    onPrimary = Color.White,
    primaryContainer = SonicRedDark,
    onPrimaryContainer = Color.White,
    secondary = SonicCyan,
    onSecondary = Color.Black,
    tertiary = SonicGreen,
    background = SonicBackground,
    onBackground = TextPrimary,
    surface = SonicSurface,
    onSurface = TextPrimary,
    surfaceVariant = SonicSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SonicBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve bespoke high-tech trading theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
