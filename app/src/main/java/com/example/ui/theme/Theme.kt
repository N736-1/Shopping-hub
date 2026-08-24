package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandGreen,
    onPrimary = Color.Black,
    primaryContainer = BrandGreenContainer,
    onPrimaryContainer = BrandGreen,
    secondary = BrandGreenDark,
    onSecondary = Color.White,
    background = BrandDarkCanvas,
    onBackground = Color.White,
    surface = BrandDarkSurface,
    onSurface = Color.White,
    surfaceVariant = BrandDarkCard,
    onSurfaceVariant = BrandTextMuted,
    outline = Color(0xFF263328)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandGreenDark,
    onPrimary = Color.White,
    primaryContainer = BrandGreenLight,
    onPrimaryContainer = BrandGreenDark,
    secondary = BrandGreen,
    onSecondary = Color.Black,
    background = Color(0xFFF9FAF9),
    onBackground = BrandTextDark,
    surface = BrandWhite,
    onSurface = BrandTextDark,
    surfaceVariant = BrandGrayLight,
    onSurfaceVariant = BrandTextMuted,
    outline = BrandGrayMedium
)

@Composable
fun DropshipHubTheme(
    darkTheme: Boolean = false, // Keep crisp clean light/dark hybrid per PRD design
    dynamicColor: Boolean = false, // Enforce brand green and dark hero palette
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
