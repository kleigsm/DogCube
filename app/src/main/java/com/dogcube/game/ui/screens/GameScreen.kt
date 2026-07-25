package com.dogcube.game.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogcube.game.engine.GamePhase
import com.dogcube.game.ui.components.ControlPanel
import com.dogcube.game.ui.components.GameCanvas
import com.dogcube.game.ui.components.NextPiecePreview
import com.dogcube.game.ui.theme.ColorBackground
import com.dogcube.game.ui.theme.ColorOverlay
import com.dogcube.game.ui.theme.ColorGridLine
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.ui.theme.ColorSurface
import com.dogcube.game.viewmodel.GameViewModel
import kotlinx.coroutines.delay

private data class ClearAnimData(val count: Int, val score: Int)

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToMenu: () -> Unit
) {
    val snap by viewModel.snapshot.collectAsState()
    var showGameOverDialog by remember { mutableStateOf(false) }
    var clearAnim by remember { mutableStateOf<ClearAnimData?>(null) }
    var prevLinesCleared by remember { mutableIntStateOf(0) }
    var shakeOffset by remember { mutableStateOf(IntOffset.Zero) }

    // Detect line clears ¡ª trigger flash, popup, shake
    LaunchedEffect(snap.linesCleared) {
        val delta = snap.linesCleared - prevLinesCleared
        if (delta > 0) {
            val points = when (delta) { 1 -> 100; 2 -> 300; 3 -> 500; 4 -> 800; else -> 0 } * snap.level
            clearAnim = ClearAnimData(delta, points)
            if (delta == 4) {
                shakeOffset = IntOffset(8, -2); delay(35)
                shakeOffset = IntOffset(-6, 3); delay(35)
                shakeOffset = IntOffset(4, -1); delay(35)
                shakeOffset = IntOffset(-3, 0); delay(35)
                shakeOffset = IntOffset(0, 0)
            }
            delay(500)
            clearAnim = null
        }
        prevLinesCleared = snap.linesCleared
    }

    LaunchedEffect(snap.phase) {
        if (snap.phase == GamePhase.GAME_OVER) showGameOverDialog = true
    }

    Box(Modifier.fillMaxSize().background(ColorBackground)) {
        Column(Modifier.fillMaxSize().imePadding()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\u5206\u6570\uFF1A${snap.score}", color = ColorScoreText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("\u7B49\u7EA7\uFF1A${snap.level}", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
                    Text("\u884C\u6570\uFF1A${snap.linesCleared}", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
                    TextButton(onClick = { viewModel.togglePause() }) {
                        Text(if (snap.phase == GamePhase.PAUSED) "\u25B6" else "\u23F8", color = ColorScoreText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Game area
            Row(Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.Top) {
                val pf = (snap.linesCleared % 10).coerceIn(0, 9).toFloat() / 10f
                Box(Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp).width(8.dp).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(ColorGridLine)) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                        Box(Modifier.fillMaxWidth().fillMaxHeight(pf.coerceIn(0f, 1f)).clip(RoundedCornerShape(4.dp)).background(ColorPrimary))
                    }
                }

                GameCanvas(snap, Modifier.weight(1f).offset { shakeOffset }, flashRows = clearAnim?.count ?: 0)

                Column(Modifier.padding(end = 8.dp, top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\u4E0B\u4E00\u4E2A", color = ColorScoreText, style = MaterialTheme.typography.labelLarge)
                    NextPiecePreview(snap.nextPiece)
                }
            }

            ControlPanel(viewModel::moveLeft, viewModel::moveRight, viewModel::softDrop, viewModel::hardDrop, viewModel::rotateCW, viewModel::rotateCCW)
            Spacer(Modifier.height(8.dp))
        }

        // Score popup
        if (clearAnim != null) {
            val fade by animateFloatAsState(targetValue = 1f, animationSpec = tween(100, 300), label = "popup")
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "+${clearAnim!!.score}",
                    color = Color(0xFFFFD700).copy(alpha = fade),
                    fontSize = if (clearAnim!!.count == 4) 48.sp else 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Pause overlay
        if (snap.phase == GamePhase.PAUSED) {
            Box(Modifier.fillMaxSize().background(ColorOverlay).clickable { viewModel.togglePause() }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\u5DF2\u6682\u505C", color = ColorScoreText, fontSize = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = 8.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("\u70B9\u51FB\u4EFB\u610F\u5904\u7EE7\u7EED", color = ColorScoreText.copy(alpha = 0.6f), fontSize = 16.sp, fontWeight = FontWeight.Normal, letterSpacing = 2.sp)
                }
            }
        }
    }

    if (showGameOverDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = { showGameOverDialog = false; onBackToMenu() }) { Text("\u83DC\u5355", color = ColorScoreText) }
                    Button(onClick = { showGameOverDialog = false; viewModel.startGame() }, colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)) {
                        Text("\u518D\u6765\u4E00\u5C40", fontWeight = FontWeight.Bold)
                    }
                }
            },
            containerColor = ColorSurface,
            title = { Text("\u6E38\u620F\u7ED3\u675F", color = ColorScoreText, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("${snap.score}", color = ColorScoreText, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("\u7B49\u7EA7 ${snap.level}  |  \u6D88\u9664 ${snap.linesCleared} \u884C", color = ColorScoreText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        )
    }
}
