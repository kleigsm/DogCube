package com.dogcube.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.dogcube.game.engine.PieceType
import com.dogcube.game.ui.theme.ColorBoardBg
import com.dogcube.game.ui.theme.ColorGridLine

@Composable
fun NextPiecePreview(piece: PieceType?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(88.dp)
            .border(2.dp, ColorGridLine, RoundedCornerShape(8.dp))
            .background(ColorBoardBg, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        if (piece == null) return@Box
        val shape = piece.shapes[0]
        Canvas(modifier = Modifier.size(80.dp)) {
            val cs = size.width / 4f
            val ox = ((4 - shape[0].size) / 2f) * cs
            val oy = ((4 - shape.size) / 2f) * cs
            for (row in shape.indices) for (col in shape[row].indices) {
                if (shape[row][col] == 0) continue
                drawRect(piece.breed.color, Offset(ox + col * cs + 1, oy + row * cs + 1), Size(cs - 2, cs - 2))
            }
        }
    }
}