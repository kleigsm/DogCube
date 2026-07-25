package com.dogcube.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.model.ScoreRecord
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorScoreText

@Composable
fun ScoreScreen(scores: List<ScoreRecord>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(ColorBoardBg).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("\u6392\u884C\u699C", color = ColorScoreText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onBack) { Text("\u8FD4\u56DE", color = ColorScoreText) }
        }
        Spacer(Modifier.height(16.dp))
        if (scores.isEmpty()) {
            Text("\u6682\u65E0\u8BB0\u5F55\uFF01", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(scores) { r ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(r.playerName, color = ColorScoreText)
                        Text("${r.score}", color = ColorScoreText, fontWeight = FontWeight.Bold)
                        Text("\u7B49\u7EA7${r.level}", color = ColorScoreText)
                        Text(r.date, color = ColorScoreText, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}