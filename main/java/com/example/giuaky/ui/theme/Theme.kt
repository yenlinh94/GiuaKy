package com.example.giuaky.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = PastelPurpleDark,
    secondary = PastelPink,
    tertiary = PastelBlue,
    background = Color(0xFF1E1B2E),
    surface = Color(0xFF2A2438),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFEDE9FE),
    onSurface = Color(0xFFEDE9FE)
)

// Light mode — surface = trắng để khung nổi
private val LightColorScheme = lightColorScheme(
    primary = PastelPurpleDark,
    secondary = PastelPink,
    tertiary = PastelBlue,
    background = PastelLavender,
    surface = PastelSurface,
    onPrimary = Color.White,
    onSecondary = PastelTextDark,
    onTertiary = PastelTextDark,
    onBackground = PastelTextDark,
    onSurface = PastelTextDark
)

@Composable
fun GiuaKyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
