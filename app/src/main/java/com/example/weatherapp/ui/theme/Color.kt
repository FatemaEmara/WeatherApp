package com.example.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


private val DarkBgTop = Color(0xFF0D1B3E)
private val DarkBgBottom = Color(0xFF0A0F2C)
private val DarkCardBg = Color(0xFF1A2550)
private val DarkCardBorder = Color(0xFF2A3560)
private val DarkTextPrimary = Color(0xFFFFFFFF)
private val DarkTextMuted = Color(0xFFB0BCDD)
private val DarkAccent = Color(0xFF6C8EFF)
private val DarkNavBg = Color(0xFF111B3A)
private val DarkNavInactive = Color(0xFF6B7BA4)
private val DarkDanger = Color(0xFFFF5C5C)

private val LightBgTop = Color(0xFFE8EFFE)
private val LightBgBottom = Color(0xFFD0D9F8)
private val LightCardBg = Color(0xFFFFFFFF)
private val LightCardBorder = Color(0xFFCDD5F0)
private val LightTextPrimary = Color(0xFF0D1B3E)
private val LightTextMuted = Color(0xFF5A6A99)
private val LightAccent = Color(0xFF3D5BDB)
private val LightNavBg = Color(0xFFFFFFFF)
private val LightNavInactive = Color(0xFF8A99CC)
private val LightDanger = Color(0xFFD93025)

data class WeatherColors(
    val bgTop: Color,
    val bgBottom: Color,
    val cardBg: Color,
    val cardBorder: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val accent: Color,
    val navBg: Color,
    val navInactive: Color,
    val danger: Color,
)


val DarkColors = WeatherColors(
    bgTop = DarkBgTop,
    bgBottom = DarkBgBottom,
    cardBg = DarkCardBg,
    cardBorder = DarkCardBorder,
    textPrimary = DarkTextPrimary,
    textMuted = DarkTextMuted,
    accent = DarkAccent,
    navBg = DarkNavBg,
    navInactive = DarkNavInactive,
    danger = DarkDanger,
)

val LightColors = WeatherColors(
    bgTop = LightBgTop,
    bgBottom = LightBgBottom,
    cardBg = LightCardBg,
    cardBorder = LightCardBorder,
    textPrimary = LightTextPrimary,
    textMuted = LightTextMuted,
    accent = LightAccent,
    navBg = LightNavBg,
    navInactive = LightNavInactive,
    danger = LightDanger,
)


val LocalWeatherColors = staticCompositionLocalOf { DarkColors }

object AppTheme {
    val colors: WeatherColors
        @Composable get() = LocalWeatherColors.current
}


@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalWeatherColors provides colors,
        content = content
    )
}

