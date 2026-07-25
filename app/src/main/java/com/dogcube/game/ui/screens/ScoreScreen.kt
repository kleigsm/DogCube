package com.dogcube.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogcube.game.model.ScoreRecord
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.ui.theme.ColorSurface

private val MedalGold = Color(0xFFFFD700)
private val MedalSilver = Color(0xFFC0C0C0)
private val MedalBronze = Color(0xFFCD7F32)

@Composable
fun ScoreScreen(scores: List<ScoreRecord>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(ColorBoardBg).padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "\u6392\u884C\u699C",
                color = ColorScoreText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onBack) {
                Text("\u25C0 \u8FD4\u56DE", color = ColorScoreText)
            }
        }
        Spacer(Modifier.height(16.dp))
        if (scores.isEmpty()) {
            Box(
                Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "\uD83C\uDFC6",
                        fontSize = 48.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "\u6682\u65E0\u8BB0\u5F55\uFF01",
                        color = ColorScoreText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "\u5FEB\u53BB\u73A9\u4E00\u5C40\u5427",
                        color = ColorScoreText.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(scores) { index, record ->
                    val medalColor = when (index) {
                        0 -> MedalGold
                        1 -> MedalSilver
                        2 -> MedalBronze
                        else -> Color.Transparent
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (medalColor != Color.Transparent)
                                medalColor.copy(alpha = 0.12f)
                            else
                                ColorSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank badge
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (medalColor != Color.Transparent) medalColor
                                        else ColorScoreText.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    color = if (index < 3) Color.Black else ColorScoreText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            // Name + date
                            Column(Modifier.weight(1f)) {
                                Text(
                                    record.playerName,
                                    color = ColorScoreText,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    record.date,
                                    color = ColorScoreText.copy(alpha = 0.5f),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            // Score
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${record.score}",
                                    color = ColorScoreText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Text(
                                    "\u7B49\u7EA7${record.level}  \u6D88${record.linesCleared}\u884C",
                                    color = ColorScoreText.copy(alpha = 0.5f),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}