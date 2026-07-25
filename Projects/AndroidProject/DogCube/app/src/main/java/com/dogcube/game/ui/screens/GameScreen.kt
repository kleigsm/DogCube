package com.dogcube.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.engine.GamePhase
import com.dogcube.game.ui.components.ControlPanel
import com.dogcube.game.ui.components.GameCanvas
import com.dogcube.game.ui.components.NextPiecePreview
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameOver: () -> Unit
) {
    val snap by viewModel.snapshot.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBoardBg)
            .imePadding()
    ) {
        // Score header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SCORE: ${snap.score}",
                color = ColorScoreText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "LV:${snap.level}",
                    color = ColorScoreText,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "LN:${snap.linesCleared}",
                    color = ColorScoreText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Game area + next piece
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            GameCanvas(
                snapshot = snap,
                modifier = Modifier.weight(1f)
            )

            Column(
                modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEXT",
                    color = ColorScoreText,
                    style = MaterialTheme.typography.labelLarge
                )
                NextPiecePreview(piece = snap.nextPiece)
            }
        }

        // Controls
        ControlPanel(
            onLeft = viewModel::moveLeft,
            onRight = viewModel::moveRight,
            onSoftDrop = viewModel::softDrop,
            onHardDrop = viewModel::hardDrop,
            onRotateCW = viewModel::rotateCW,
            onRotateCCW = viewModel::rotateCCW
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    // Game over overlay
    if (snap.phase == GamePhase.GAME_OVER) {
        onGameOver()
    }
}
