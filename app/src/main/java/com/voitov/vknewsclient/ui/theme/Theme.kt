package com.voitov.vknewsclient.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = CoolBlack,
    primaryVariant = CoolBlack,
    secondary = CoolBlack,
    background = Color.Black,
    surface = CoolBlack,
    onPrimary = Color.White,
    onSecondary = CoolGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Color.White,
    background = Color(237, 238, 240),
    surface = Color.White,
    onPrimary = CoolBlack,
    onSecondary = CoolGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun VkNewsClientTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}