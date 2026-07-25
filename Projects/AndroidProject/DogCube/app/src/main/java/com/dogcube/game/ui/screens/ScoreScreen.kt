package com.dogcube.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.model.ScoreRecord
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorScoreText

@Composable
fun ScoreScreen(
    scores: List<ScoreRecord>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBoardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HIGH SCORES",
                color = ColorScoreText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onBack) {
                Text("BACK", color = ColorScoreText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (scores.isEmpty()) {
            Text(
                text = "No scores yet!",
                color = ColorScoreText,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scores) { record ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = record.playerName,
                            color = ColorScoreText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${record.score}",
                            color = ColorScoreText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "LV.${record.level}",
                            color = ColorScoreText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = record.date,
                            color = ColorScoreText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
