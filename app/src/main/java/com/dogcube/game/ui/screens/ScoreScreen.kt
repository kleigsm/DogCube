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
            Text("排行榜", color = ColorScoreText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onBack) { Text("返回", color = ColorScoreText) }
        }
        Spacer(Modifier.height(16.dp))
        if (scores.isEmpty()) Text("暂无记录！", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(scores) { r ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(r.playerName, color = ColorScoreText); Text("{r.score}", color = ColorScoreText, fontWeight = FontWeight.Bold)
                    Text("LV.{r.level}", color = ColorScoreText); Text(r.date, color = ColorScoreText, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
