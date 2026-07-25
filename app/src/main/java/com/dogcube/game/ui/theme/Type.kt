package com.dogcube.game.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DogCubeTypography = Typography(
    // Page title: "DogCube", "排行榜", "游戏结束"
    headlineLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 30.sp, letterSpacing = 0.sp, lineHeight = 36.sp),
    // Section title: "下一个", "PAUSED"
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = 0.sp, lineHeight = 28.sp),
    // Data label: "分数：", "等级：", "行数："
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = 0.sp, lineHeight = 24.sp),
    // Body text: score numbers, dialog body
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 0.sp, lineHeight = 22.sp),
    // Hint text: "暂无记录！", "点击任意处继续"
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 14.sp, letterSpacing = 0.5.sp, lineHeight = 20.sp),
    // Small labels: "NEXT", date text
    labelLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 0.5.sp, lineHeight = 16.sp)
)
