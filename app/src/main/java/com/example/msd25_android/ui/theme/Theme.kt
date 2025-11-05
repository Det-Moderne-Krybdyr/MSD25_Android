package com.example.msd25_android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    secondary = GreenLight,
    onSecondary = TextPrimary,
    tertiary = GreenDark,
    onTertiary = Color.White,

    background = WhiteBackground,
    surface = WhiteBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,

    primaryContainer = GreenLight.copy(alpha = 0.22f),
    onPrimaryContainer = GreenDark,
    secondaryContainer = GreenLight.copy(alpha = 0.18f),
    onSecondaryContainer = GreenDark,
    tertiaryContainer = GreenLight.copy(alpha = 0.18f),
    onTertiaryContainer = GreenDark,

    surfaceVariant = GreenLight.copy(alpha = 0.14f),
    onSurfaceVariant = GreenDark,
    outline = GreenLight.copy(alpha = 0.45f),
    outlineVariant = GreenLight.copy(alpha = 0.25f),
    surfaceTint = GreenPrimary,

    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = GreenLight,
    onPrimary = Color.Black,
    secondary = GreenDark,
    onSecondary = Color.White,
    tertiary = GreenLight,
    onTertiary = Color.Black,

    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White,

    primaryContainer = GreenDark,
    onPrimaryContainer = Color.White,
    secondaryContainer = GreenDark,
    onSecondaryContainer = Color.White,
    tertiaryContainer = GreenDark,
    onTertiaryContainer = Color.White,

    surfaceVariant = Color(0xFF2A2F2A),
    onSurfaceVariant = Color(0xFFE0E3E0),
    outline = Color(0xFF3C433C),
    outlineVariant = Color(0xFF2F352F),
    surfaceTint = GreenLight,

    error = ErrorRed
)


@Composable
fun MSD25_AndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
