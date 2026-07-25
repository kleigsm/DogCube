package com.dogcube.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dogcube.game.engine.Board
import com.dogcube.game.engine.GameSnapshot
import com.dogcube.game.model.DogBreed
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorGhostPiece
import com.dogcube.game.ui.theme.ColorGridLine

@Composable
fun GameCanvas(snapshot: GameSnapshot, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(Board.WIDTH.toFloat() / Board.HEIGHT.toFloat()).padding(horizontal = 16.dp)) {
        val cw = size.width / Board.WIDTH
        val ch = size.height / Board.HEIGHT

        // Background
        drawRect(ColorBoardBg, size = size)

        // Grid lines ¡ª thicker for visibility
        for (x in 0..Board.WIDTH) {
            drawLine(ColorGridLine, Offset(x * cw, 0f), Offset(x * cw, size.height), 1.5f)
        }
        for (y in 0..Board.HEIGHT) {
            drawLine(ColorGridLine, Offset(0f, y * ch), Offset(size.width, y * ch), 1.5f)
        }

        // Locked cells with subtle border
        for (y in snapshot.board.indices) {
            for (x in snapshot.board[y].indices) {
                val v = snapshot.board[y][x]
                if (v > 0) {
                    val color = DogBreed.entries[v - 1].color
                    // Main fill
                    drawRect(color, Offset(x * cw + 1, y * ch + 1), Size(cw - 2, ch - 2))
                    // Top edge highlight (lighter)
                    drawLine(color.copy(alpha = 0.45f), Offset(x * cw + 1, y * ch + 1), Offset(x * cw + cw - 1, y * ch + 1), 1.5f)
                    drawLine(color.copy(alpha = 0.45f), Offset(x * cw + 1, y * ch + 1), Offset(x * cw + 1, y * ch + ch - 1), 1.5f)
                    // Bottom edge shadow (darker)
                    drawLine(Color.Black.copy(alpha = 0.25f), Offset(x * cw + 1, y * ch + ch - 1), Offset(x * cw + cw - 1, y * ch + ch - 1), 1.5f)
                    drawLine(Color.Black.copy(alpha = 0.25f), Offset(x * cw + cw - 1, y * ch + 1), Offset(x * cw + cw - 1, y * ch + ch - 1), 1.5f)
                }
            }
        }

        val p = snapshot.currentPiece ?: return@Canvas
        val shape = p.shapes[snapshot.currentRotation]
        val breedColor = p.breed.color

        // Ghost piece
        val gY = snapshot.ghostY
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val py = gY + row
                if (py < 0) continue
                drawRect(ColorGhostPiece, Offset((snapshot.currentX + col) * cw + 1, py * ch + 1), Size(cw - 2, ch - 2))
            }
        }

        // Active piece with 3D highlight
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val py = snapshot.currentY + row
                if (py < 0) continue
                val ox = (snapshot.currentX + col) * cw
                val oy = py * ch
                drawRect(breedColor, Offset(ox + 1, oy + 1), Size(cw - 2, ch - 2))
                drawLine(breedColor.copy(alpha = 0.5f), Offset(ox + 1, oy + 1), Offset(ox + cw - 1, oy + 1), 1.5f)
                drawLine(breedColor.copy(alpha = 0.5f), Offset(ox + 1, oy + 1), Offset(ox + 1, oy + ch - 1), 1.5f)
                drawLine(Color.Black.copy(alpha = 0.2f), Offset(ox + 1, oy + ch - 1), Offset(ox + cw - 1, oy + ch - 1), 1.5f)
                drawLine(Color.Black.copy(alpha = 0.2f), Offset(ox + cw - 1, oy + 1), Offset(ox + cw - 1, oy + ch - 1), 1.5f)
            }
        }
    }
}