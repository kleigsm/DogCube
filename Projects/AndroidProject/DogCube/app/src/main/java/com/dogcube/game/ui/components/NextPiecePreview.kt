package com.dogcube.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.dogcube.game.engine.PieceType
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorGridLine

@Composable
fun NextPiecePreview(
    piece: PieceType?,
    modifier: Modifier = Modifier
) {
    if (piece == null) return

    val shape = piece.shapes[0]
    val rows = shape.size
    val cols = shape[0].size

    Canvas(
        modifier = modifier.size(80.dp)
    ) {
        val cellSize = size.width / 4f

        drawRect(ColorBoardBg, size = size)

        // Offset to center the piece
        val offsetX = ((4 - cols) / 2f) * cellSize
        val offsetY = ((4 - rows) / 2f) * cellSize

        for (row in shape.indices) {
            for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                drawRect(
                    color = piece.breed.color,
                    topLeft = Offset(offsetX + col * cellSize + 1, offsetY + row * cellSize + 1),
                    size = Size(cellSize - 2, cellSize - 2)
                )
            }
        }
    }
}
