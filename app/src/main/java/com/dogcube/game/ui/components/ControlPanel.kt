package com.dogcube.game.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogcube.game.ui.theme.ColorPrimary
import com.dogcube.game.ui.theme.ColorScoreText
import com.dogcube.game.ui.theme.Dimens

@Composable
fun ControlPanel(
    onLeft: () -> Unit, onRight: () -> Unit, onSoftDrop: () -> Unit, onHardDrop: () -> Unit,
    onRotateCW: () -> Unit, onRotateCCW: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = Dimens.GAP_XL, vertical = Dimens.GAP_SM),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.GAP_MD)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.GAP_XXL)) {
            PressableButton("\u21BA", onRotateCCW, Dimens.BTN_SMALL_W, 48.dp)
            PressableButton("\u21BB", onRotateCW, Dimens.BTN_SMALL_W, 48.dp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.GAP_MD), verticalAlignment = Alignment.CenterVertically) {
            PressableButton("\u25C0", onLeft, Dimens.BTN_MED_W, 60.dp)
            PressableButton("\u25BC", onSoftDrop, Dimens.BTN_MED_W, 60.dp)
            PressableButton("\u25B6", onRight, Dimens.BTN_MED_W, 60.dp)
        }
        PressableButton("\u25BC\u25BC", onHardDrop, Dimens.BTN_DROP_W, 44.dp)
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
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = tween(80),
        label = "pressScale"
    )
    Box(
        modifier = Modifier
            .size(width, height)
            .scale(scale)
            .clip(RoundedCornerShape(Dimens.BTN_RADIUS))
            .background(if (pressed) ColorPrimary.copy(alpha = 0.55f) else ColorPrimary)
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
            fontSize = 26.sp,
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
