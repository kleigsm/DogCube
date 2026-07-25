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


@Composable
fun NextPiecePreview(piece: PieceType?, modifier: Modifier = Modifier) {
    if (piece == null) return
    val shape = piece.shapes[0]
    Canvas(modifier = modifier.size(80.dp)) {
        val cs = size.width / 4f
        drawRect(ColorBoardBg, size = size)
        val ox = ((4 - shape[0].size) / 2f) * cs; val oy = ((4 - shape.size) / 2f) * cs
        for (row in shape.indices) for (col in shape[row].indices) {
            if (shape[row][col] == 0) continue
            drawRect(piece.breed.color, Offset(ox + col * cs + 1, oy + row * cs + 1), Size(cs - 2, cs - 2))
        }
    }
}
