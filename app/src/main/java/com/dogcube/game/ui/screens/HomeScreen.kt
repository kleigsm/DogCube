package com.dogcube.game.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.ui.theme.ColorSurface
import kotlin.random.Random

private data class FallingBlock(
    val xFraction: Float,
    val phaseOffset: Float,
    val size: Float,
    val color: Color,
    val alpha: Float
)

@Composable
fun HomeScreen(onStartGame: () -> Unit, onShowScores: () -> Unit) {
    val breeds = listOf(
        Color(0xFF00BCD4), Color(0xFFFFC107), Color(0xFF9C27B0),
        Color(0xFF4CAF50), Color(0xFFF44336), Color(0xFF2196F3), Color(0xFFFF9800)
    )
    val blocks = remember {
        val rng = Random(42)
        List(18) {
            FallingBlock(
                xFraction = rng.nextFloat(),
                phaseOffset = rng.nextFloat(),
                size = rng.nextFloat() * 16f + 8f,
                color = breeds[rng.nextInt(breeds.size)],
                alpha = rng.nextFloat() * 0.08f + 0.04f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "fall")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "fallProgress"
    )

    Box(modifier = Modifier.fillMaxSize().background(ColorBoardBg)) {
        Canvas(Modifier.fillMaxSize()) {
            for (b in blocks) {
                val cx = b.xFraction * size.width
                val cy = ((progress + b.phaseOffset) % 1.0f) * size.height
                val bw = b.size * 2.5f
                val bh = b.size * 2.5f
                drawRect(b.color.copy(alpha = b.alpha), Offset(cx - bw / 2, cy - bh / 2), Size(bw, bh))
                drawRect(Color.White.copy(alpha = b.alpha * 0.3f), Offset(cx - bw / 2 + 2, cy - bh / 2 + 2), Size(bw - 4, 4f))
            }
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                Modifier.padding(horizontal = 32.dp).clip(RoundedCornerShape(16.dp))
                    .background(ColorSurface.copy(alpha = 0.85f)).padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\uD83D\uDC36 DogCube", color = ColorScoreText, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("\uD83D\uDC15 \uD83E\uDD8A \uD83D\uDC3A \uD83D\uDC29 \uD83D\uDC3E", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onStartGame,
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                modifier = Modifier.size(220.dp, 60.dp).shadow(8.dp, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("\u5F00\u59CB\u6E38\u620F", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onShowScores) {
                Text("\u6392\u884C\u699C", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
