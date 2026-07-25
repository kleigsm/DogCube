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
import com.dogcube.game.ui.theme.ColorBoardGlow
import com.dogcube.game.ui.theme.ColorFlashGold
import com.dogcube.game.ui.theme.ColorFlashWhite
import com.dogcube.game.ui.theme.ColorGhostPiece
import com.dogcube.game.ui.theme.ColorGridLine
import com.dogcube.game.ui.theme.ColorPrimary
import kotlin.math.min

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

        val glowW = 3.dp.toPx()
        drawRect(ColorBoardGlow, Offset(-glowW, -glowW), Size(size.width + glowW * 2, size.height + glowW * 2), style = androidx.compose.ui.graphics.drawscope.Stroke(width = glowW))
        drawRect(ColorPrimary.copy(alpha = 0.10f), Offset(0f, 0f), size, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx()))

        for (x in 0..Board.WIDTH) drawLine(ColorGridLine, Offset(x * cw, 0f), Offset(x * cw, size.height), 1.5f)
        for (y in 0..Board.HEIGHT) drawLine(ColorGridLine, Offset(0f, y * ch), Offset(size.width, y * ch), 1.5f)

        // Locked cells — 3D gradient tile style
        for (y in snapshot.board.indices) for (x in snapshot.board[y].indices) {
            val v = snapshot.board[y][x]
            if (v > 0) {
                val base = DogBreed.entries[v - 1].color
                val pad = 1.2f
                val inner = 3f
                val edgeW = 2.5f
                val ox = x * cw; val oy = y * ch

                // Darker base
                val dark = Color(red = base.red * 0.75f, green = base.green * 0.75f, blue = base.blue * 0.75f)
                drawRect(dark, Offset(ox + pad, oy + pad), Size(cw - pad * 2, ch - pad * 2))

                // Brighter inner center (gradient highlight)
                val bright = Color(red = min(1f, base.red * 1.35f), green = min(1f, base.green * 1.35f), blue = min(1f, base.blue * 1.35f), alpha = 0.35f)
                drawRect(bright, Offset(ox + inner, oy + inner), Size(cw - inner * 2, ch - inner * 2))

                // Base color fill (main visible layer, slightly inset)
                drawRect(base, Offset(ox + pad + 1, oy + pad + 1), Size(cw - pad * 2 - 2, ch - pad * 2 - 2))

                // Top + left highlight edges (bright)
                drawLine(base.copy(alpha = 0.6f), Offset(ox + pad, oy + pad), Offset(ox + cw - pad, oy + pad), edgeW)
                drawLine(base.copy(alpha = 0.6f), Offset(ox + pad, oy + pad), Offset(ox + pad, oy + ch - pad), edgeW)

                // Bottom + right shadow edges (dark)
                val shadow = Color(red = base.red * 0.35f, green = base.green * 0.35f, blue = base.blue * 0.35f, alpha = 0.6f)
                drawLine(shadow, Offset(ox + pad, oy + ch - pad), Offset(ox + cw - pad, oy + ch - pad), edgeW)
                drawLine(shadow, Offset(ox + cw - pad, oy + pad), Offset(ox + cw - pad, oy + ch - pad), edgeW)

                // White specular dot top-left
                drawRect(Color.White.copy(alpha = 0.4f), Offset(ox + pad + 2, oy + pad + 2), Size(min(cw * 0.25f, 8f), min(ch * 0.25f, 8f)))
            }
        }

        // Flash rows
        for (i in 0 until flashRows) {
            val row = Board.HEIGHT - 1 - i
            drawRect(ColorFlashWhite, Offset(0f, row * ch), Size(size.width, ch))
            drawRect(ColorFlashGold, Offset(0f, row * ch + 2), Size(size.width, ch - 4))
        }

        val p = snapshot.currentPiece ?: return@Canvas
        val shape = p.shapes[snapshot.currentRotation]
        val base = p.breed.color

        // Ghost piece
        val gY = snapshot.ghostY
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            val py = gY + row; if (py < 0) continue
            drawRect(ColorGhostPiece, Offset((snapshot.currentX + col) * cw + 1, py * ch + 1), Size(cw - 2, ch - 2))
        }

        // Active piece — brighter 3D tile
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            val py = snapshot.currentY + row; if (py < 0) continue
            val pad = 1.2f; val inner = 3f; val edgeW = 3f
            val ox = (snapshot.currentX + col) * cw; val oy = py * ch

            val dark = Color(red = base.red * 0.7f, green = base.green * 0.7f, blue = base.blue * 0.7f)
            drawRect(dark, Offset(ox + pad, oy + pad), Size(cw - pad * 2, ch - pad * 2))

            val bright = Color(red = min(1f, base.red * 1.4f), green = min(1f, base.green * 1.4f), blue = min(1f, base.blue * 1.4f), alpha = 0.4f)
            drawRect(bright, Offset(ox + inner, oy + inner), Size(cw - inner * 2, ch - inner * 2))

            drawRect(base, Offset(ox + pad + 1, oy + pad + 1), Size(cw - pad * 2 - 2, ch - pad * 2 - 2))

            drawLine(base.copy(alpha = 0.65f), Offset(ox + pad, oy + pad), Offset(ox + cw - pad, oy + pad), edgeW)
            drawLine(base.copy(alpha = 0.65f), Offset(ox + pad, oy + pad), Offset(ox + pad, oy + ch - pad), edgeW)

            val shadow = Color(red = base.red * 0.3f, green = base.green * 0.3f, blue = base.blue * 0.3f, alpha = 0.65f)
            drawLine(shadow, Offset(ox + pad, oy + ch - pad), Offset(ox + cw - pad, oy + ch - pad), edgeW)
            drawLine(shadow, Offset(ox + cw - pad, oy + pad), Offset(ox + cw - pad, oy + ch - pad), edgeW)

            drawRect(Color.White.copy(alpha = 0.5f), Offset(ox + pad + 2, oy + pad + 2), Size(min(cw * 0.25f, 8f), min(ch * 0.25f, 8f)))
        }
    }
}
