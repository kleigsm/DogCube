package com.dogcube.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogcube.game.engine.GamePhase
import com.dogcube.game.ui.components.ControlPanel
import com.dogcube.game.ui.components.GameCanvas
import com.dogcube.game.ui.components.NextPiecePreview
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.ui.theme.ColorSurface
import com.dogcube.game.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToMenu: () -> Unit
) {
    val snap by viewModel.snapshot.collectAsState()
    var showGameOverDialog by remember { mutableStateOf(false) }

    // Trigger game over dialog
    LaunchedEffect(snap.phase) {
        if (snap.phase == GamePhase.GAME_OVER) showGameOverDialog = true
    }

    Box(Modifier.fillMaxSize().background(ColorBoardBg)) {
        Column(Modifier.fillMaxSize().imePadding()) {
            // Header: score + pause button
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "SCORE: ${snap.score}",
                    color = ColorScoreText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("LV:${snap.level}", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
                    Text("LN:${snap.linesCleared}", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
                    // Pause button
                    TextButton(onClick = { viewModel.togglePause() }) {
                        Text(
                            if (snap.phase == GamePhase.PAUSED) "\u25B6" else "\u23F8",
                            color = ColorScoreText,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Game area
            Row(Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.Top) {
                GameCanvas(snap, Modifier.weight(1f))
                Column(Modifier.padding(end = 8.dp, top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("NEXT", color = ColorScoreText, style = MaterialTheme.typography.labelLarge)
                    NextPiecePreview(snap.nextPiece)
                }
            }

            // Controls
            ControlPanel(
                viewModel::moveLeft, viewModel::moveRight,
                viewModel::softDrop, viewModel::hardDrop,
                viewModel::rotateCW, viewModel::rotateCCW
            )
            Spacer(Modifier.height(8.dp))
        }

        // Pause overlay
        if (snap.phase == GamePhase.PAUSED) {
            Box(
                Modifier.fillMaxSize().background(ColorBoardBg.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "PAUSED",
                    color = ColorScoreText,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp
                )
            }
        }
    }

    // Game over dialog
    if (showGameOverDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = {
                        showGameOverDialog = false
                        onBackToMenu()
                    }) {
                        Text("MENU", color = ColorScoreText)
                    }
                    Button(
                        onClick = {
                            showGameOverDialog = false
                            viewModel.startGame()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
                    ) {
                        Text("PLAY AGAIN", fontWeight = FontWeight.Bold)
                    }
                }
            },
            containerColor = ColorSurface,
            title = {
                Text(
                    "GAME OVER",
                    color = ColorScoreText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "${snap.score}",
                        color = ColorScoreText,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Level ${snap.level}  |  ${snap.linesCleared} lines",
                        color = ColorScoreText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        )
    }
}