package com.dogcube.game.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText


@Composable
fun HomeScreen(onStartGame: () -> Unit, onShowScores: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(ColorBoardBg), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("\uD83D\uDC36 DogCube", color = ColorScoreText, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text("\uD83D\uDC15 \uD83E\uDD8A \uD83D\uDC3A \uD83D\uDC29 \uD83D\uDC3E", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onStartGame, colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary), modifier = Modifier.size(200.dp, 56.dp)) {
                Text("START", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }
            TextButton(onShowScores) { Text("HIGH SCORES", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge) }
        }
    }
}
