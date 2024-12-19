package com.mmb.ui_theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    surface = LightGray,
    onSurface = Color.Black,
    onSurfaceVariant = MediumGray,
    outline = LightGray,
    secondary = DarkGray,
)

@Composable
fun WeatherTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}