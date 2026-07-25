package com.dogcube.game.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val DogCubeDarkColorScheme = darkColorScheme(
    primary = ColorPrimary, secondary = ColorShiba, tertiary = ColorDachshund,
    background = ColorBoardBg, surface = ColorSurface,
    onPrimary = ColorScoreText, onBackground = ColorScoreText, onSurface = ColorScoreText
)


@Composable
fun DogCubeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DogCubeDarkColorScheme, typography = DogCubeTypography, content = content)
}
