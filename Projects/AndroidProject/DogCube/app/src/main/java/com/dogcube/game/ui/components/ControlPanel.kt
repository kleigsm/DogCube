package com.dogcube.game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dogcube.game.ui.theme.ColorPrimary

@Composable
fun ControlPanel(
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onRotateCW: () -> Unit,
    onRotateCCW: () -> Unit,
    modifier: Modifier = Modifier
) {
    val btnColors = ButtonDefaults.buttonColors(
        containerColor = ColorPrimary
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Rotation row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRotateCCW,
                colors = btnColors,
                modifier = Modifier.size(width = 120.dp, height = 48.dp)
            ) {
                Text("\u21BA", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
            Button(
                onClick = onRotateCW,
                colors = btnColors,
                modifier = Modifier.size(width = 120.dp, height = 48.dp)
            ) {
                Text("\u21BB", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
        }

        // Direction row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onLeft,
                colors = btnColors,
                modifier = Modifier.size(width = 80.dp, height = 56.dp)
            ) {
                Text("\u25C0", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
            Button(
                onClick = onSoftDrop,
                colors = btnColors,
                modifier = Modifier.size(width = 80.dp, height = 56.dp)
            ) {
                Text("\u25BC", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
            Button(
                onClick = onRight,
                colors = btnColors,
                modifier = Modifier.size(width = 80.dp, height = 56.dp)
            ) {
                Text("\u25B6", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
        }

        // Hard drop row
        Button(
            onClick = onHardDrop,
            colors = btnColors,
            modifier = Modifier.size(width = 80.dp, height = 48.dp)
        ) {
            Text("\u25BC\u25BC", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
        }
    }
}
