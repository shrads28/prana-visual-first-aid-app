package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SageGreenPrimary,
    secondary = OliveGreenSecondary,
    tertiary = ForestGreenAccent,
    background = Color(0xFF1E231E),
    surface = Color(0xFF272E27),
    onPrimary = Color(0xFF1E231E),
    onSecondary = Color(0xFFFAF8F3),
    onBackground = Color(0xFFE8ECE7),
    onSurface = Color(0xFFE8ECE7),
    error = AccentRed
)

private val LightColorScheme = lightColorScheme(
    primary = SageGreenPrimary,
    secondary = OliveGreenSecondary,
    tertiary = ForestGreenAccent,
    background = BackgroundOffWhite,
    surface = WarmBeige,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    error = AccentRed

    /* Other default colors to override
    onTertiary = Color.White,
    */
)

@Composable
fun PranaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic color to maintain cohesive earthy aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
