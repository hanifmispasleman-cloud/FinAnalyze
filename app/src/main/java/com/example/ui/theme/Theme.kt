package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FinancialDarkColorScheme = darkColorScheme(
    primary = Indigo400,
    onPrimary = Color.White,
    primaryContainer = Slate800,
    onPrimaryContainer = Indigo400,
    secondary = Emerald400,
    onSecondary = Color.Black,
    secondaryContainer = Emerald950,
    onSecondaryContainer = Emerald400,
    tertiary = Amber400,
    onTertiary = Color.Black,
    tertiaryContainer = Amber950,
    onTertiaryContainer = Amber400,
    background = Slate950,
    onBackground = Slate100,
    surface = Slate900,
    onSurface = Slate100,
    surfaceVariant = Slate800,
    onSurfaceVariant = Slate300,
    outline = Slate700,
    outlineVariant = Slate750,
    error = Rose400,
    onError = Color.White,
    errorContainer = Rose950,
    onErrorContainer = Rose400
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FinancialDarkColorScheme,
        typography = Typography,
        content = content
    )
}
