package com.dogcube.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dogcube.game.engine.Board
import com.dogcube.game.engine.GameSnapshot
import com.dogcube.game.model.DogBreed
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorGhostPiece
import com.dogcube.game.ui.theme.ColorGridLine
import kotlin.math.roundToInt

@Composable
fun GameCanvas(
    snapshot: GameSnapshot,
    modifier: Modifier = Modifier,
    flashRows: Int = 0
) {
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(Board.WIDTH.toFloat() / Board.HEIGHT.toFloat()).padding(horizontal = 16.dp)) {
        val cw = size.width / Board.WIDTH
        val ch = size.height / Board.HEIGHT

        drawRect(ColorBoardBg, size = size)

        // Glow border
        val glowWidth = 3.dp.toPx()
        drawRect(Color(0xFFFF6B8A).copy(alpha = 0.25f), Offset(-glowWidth, -glowWidth), Size(size.width + glowWidth * 2, size.height + glowWidth * 2), style = androidx.compose.ui.graphics.drawscope.Stroke(width = glowWidth))
        drawRect(Color(0xFFFF6B8A).copy(alpha = 0.10f), Offset(0f, 0f), size, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx()))

        // Grid
        for (x in 0..Board.WIDTH) drawLine(ColorGridLine, Offset(x * cw, 0f), Offset(x * cw, size.height), 1.5f)
        for (y in 0..Board.HEIGHT) drawLine(ColorGridLine, Offset(0f, y * ch), Offset(size.width, y * ch), 1.5f)

        // Locked cells
        for (y in snapshot.board.indices) for (x in snapshot.board[y].indices) {
            val v = snapshot.board[y][x]
            if (v > 0) {
                val color = DogBreed.entries[v - 1].color
                val shadow = color.copy(red = color.red * 0.6f, green = color.green * 0.6f, blue = color.blue * 0.6f)
                drawRect(color, Offset(x * cw + 1, y * ch + 1), Size(cw - 2, ch - 2))
                drawLine(color.copy(alpha = 0.5f), Offset(x * cw + 1, y * ch + 1), Offset(x * cw + cw - 1, y * ch + 1), 1.5f)
                drawLine(color.copy(alpha = 0.5f), Offset(x * cw + 1, y * ch + 1), Offset(x * cw + 1, y * ch + ch - 1), 1.5f)
                drawLine(shadow.copy(alpha = 0.3f), Offset(x * cw + 1, y * ch + ch - 1), Offset(x * cw + cw - 1, y * ch + ch - 1), 1.5f)
                drawLine(shadow.copy(alpha = 0.3f), Offset(x * cw + cw - 1, y * ch + 1), Offset(x * cw + cw - 1, y * ch + ch - 1), 1.5f)
            }
        }

        // Flash rows for line clear animation
        for (i in 0 until flashRows) {
            val row = Board.HEIGHT - 1 - i
            drawRect(Color.White.copy(alpha = 0.7f), Offset(0f, row * ch), Size(size.width, ch))
            drawRect(Color(0xFFFFD700).copy(alpha = 0.3f), Offset(0f, row * ch + 2), Size(size.width, ch - 4))
        }

        val p = snapshot.currentPiece ?: return@Canvas
        val shape = p.shapes[snapshot.currentRotation]
        val breedColor = p.breed.color
        val shadowColor = breedColor.copy(red = breedColor.red * 0.6f, green = breedColor.green * 0.6f, blue = breedColor.blue * 0.6f)

        // Ghost
        val gY = snapshot.ghostY
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            val py = gY + row; if (py < 0) continue
            drawRect(ColorGhostPiece, Offset((snapshot.currentX + col) * cw + 1, py * ch + 1), Size(cw - 2, ch - 2))
        }

        // Active piece
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            val py = snapshot.currentY + row; if (py < 0) continue
            val ox = (snapshot.currentX + col) * cw; val oy = py * ch
            drawRect(breedColor, Offset(ox + 1, oy + 1), Size(cw - 2, ch - 2))
            drawLine(breedColor.copy(alpha = 0.5f), Offset(ox + 1, oy + 1), Offset(ox + cw - 1, oy + 1), 1.5f)
            drawLine(breedColor.copy(alpha = 0.5f), Offset(ox + 1, oy + 1), Offset(ox + 1, oy + ch - 1), 1.5f)
            drawLine(shadowColor.copy(alpha = 0.3f), Offset(ox + 1, oy + ch - 1), Offset(ox + cw - 1, oy + ch - 1), 1.5f)
            drawLine(shadowColor.copy(alpha = 0.3f), Offset(ox + cw - 1, oy + 1), Offset(ox + cw - 1, oy + ch - 1), 1.5f)
        }
    }
}
