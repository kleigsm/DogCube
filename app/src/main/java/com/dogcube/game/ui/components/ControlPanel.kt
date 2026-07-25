package com.dogcube.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText

@Composable
fun ControlPanel(
    onLeft: () -> Unit, onRight: () -> Unit, onSoftDrop: () -> Unit, onHardDrop: () -> Unit,
    onRotateCW: () -> Unit, onRotateCCW: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Rotation row
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            PressableButton("\u21BA", onRotateCCW, 64.dp, 48.dp)
            PressableButton("\u21BB", onRotateCW, 64.dp, 48.dp)
        }

        // D-pad row
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            PressableButton("\u25C0", onLeft, 72.dp, 60.dp)
            PressableButton("\u25BC", onSoftDrop, 72.dp, 60.dp)
            PressableButton("\u25B6", onRight, 72.dp, 60.dp)
        }

        // Hard drop
        PressableButton("\u25BC\u25BC", onHardDrop, 100.dp, 44.dp)
    }
}

@Composable
private fun PressableButton(
    label: String,
    onClick: () -> Unit,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp
) {
    var pressed by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(width, height)
            .clip(RoundedCornerShape(10.dp))
            .background(if (pressed) ColorPrimary.copy(alpha = 0.6f) else ColorPrimary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                pressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = ColorScoreText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(80)
            pressed = false
        }
    }
}