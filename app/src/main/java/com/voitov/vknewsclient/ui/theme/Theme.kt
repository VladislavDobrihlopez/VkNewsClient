package com.voitov.vknewsclient.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = CoolBlack,
    primaryVariant = Color.Black,
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
    primaryVariant = CoolWhite,
    secondary = Color.White,
    background = CoolWhite,
    surface = Color.White,
    onPrimary = CoolBlack,
    onSecondary = CoolGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@RequiresApi(Build.VERSION_CODES.P)
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

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = colors.primaryVariant,
            darkIcons = !darkTheme
        )

        systemUiController.setNavigationBarColor(
            color = colors.primaryVariant,
            darkIcons = !darkTheme
        )
    }
}