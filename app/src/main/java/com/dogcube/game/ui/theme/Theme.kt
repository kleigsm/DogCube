package com.dogcube.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DogCubeLightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    secondary = ColorSecondary,
    tertiary = ColorDachshund,
    background = ColorBackground,
    surface = ColorSurface,
    onPrimary = ColorOnPrimary,
    onBackground = ColorOnBackground,
    onSurface = ColorOnSurface
)

@Composable
fun DogCubeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DogCubeLightColorScheme,
        typography = DogCubeTypography,
        content = content
    )
}
