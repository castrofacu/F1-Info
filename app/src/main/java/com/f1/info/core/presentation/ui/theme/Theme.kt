package com.f1.info.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = F1Red,
    secondary = Carbon300,
    tertiary = Carbon500,
    background = Carbon900,
    surface = Carbon800,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = White,
    onSurface = White,
    error = Error,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = F1Red,
    secondary = Carbon700,
    tertiary = Carbon200,
    background = Carbon50,
    surface = White,
    onPrimary = White,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = Carbon900,
    onSurface = Carbon900,
    error = Error,
    onError = White
)

@Composable
fun F1InfoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}