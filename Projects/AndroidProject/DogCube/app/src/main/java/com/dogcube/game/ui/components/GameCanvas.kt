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
import com.dogcube.game.engine.PieceType
import com.dogcube.game.model.DogBreed
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorGhostPiece
import com.dogcube.game.ui.theme.ColorGridLine

@Composable
fun GameCanvas(
    snapshot: GameSnapshot,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(Board.WIDTH.toFloat() / Board.HEIGHT.toFloat())
            .padding(horizontal = 16.dp)
    ) {
        val cellW = size.width / Board.WIDTH
        val cellH = size.height / Board.HEIGHT

        // Background
        drawRect(ColorBoardBg, size = size)

        // Grid lines
        for (x in 0..Board.WIDTH) {
            drawLine(
                color = ColorGridLine,
                start = Offset(x * cellW, 0f),
                end = Offset(x * cellW, size.height),
                strokeWidth = 1f
            )
        }
        for (y in 0..Board.HEIGHT) {
            drawLine(
                color = ColorGridLine,
                start = Offset(0f, y * cellH),
                end = Offset(size.width, y * cellH),
                strokeWidth = 1f
            )
        }

        // Locked cells
        val grid = snapshot.board
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                val v = grid[y][x]
                if (v > 0) {
                    val breed = DogBreed.entries[v - 1]
                    drawRect(
                        color = breed.color,
                        topLeft = Offset(x * cellW + 1, y * cellH + 1),
                        size = Size(cellW - 2, cellH - 2)
                    )
                }
            }
        }

        // Ghost piece
        val piece = snapshot.currentPiece ?: return@Canvas
        val gY = snapshot.ghostY
        val shape = piece.shapes[snapshot.currentRotation]
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val py = gY + row
                if (py < 0) continue
                drawRect(
                    color = ColorGhostPiece,
                    topLeft = Offset((snapshot.currentX + col) * cellW + 1, py * cellH + 1),
                    size = Size(cellW - 2, cellH - 2)
                )
            }
        }

        // Active piece
        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                val py = snapshot.currentY + row
                if (py < 0) continue
                drawRect(
                    color = piece.breed.color,
                    topLeft = Offset((snapshot.currentX + col) * cellW + 1, py * cellH + 1),
                    size = Size(cellW - 2, cellH - 2)
                )
            }
        }
    }
}
