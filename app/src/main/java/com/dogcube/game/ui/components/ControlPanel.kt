package com.dogcube.game.ui.components


import androidx.compose.foundation.layout.*
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
    onLeft: () -> Unit, onRight: () -> Unit, onSoftDrop: () -> Unit, onHardDrop: () -> Unit,
    onRotateCW: () -> Unit, onRotateCCW: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bc = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onRotateCCW, bc, Modifier.size(120.dp, 48.dp)) { Text("\u21BA", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize) }
            Button(onRotateCW, bc, Modifier.size(120.dp, 48.dp)) { Text("\u21BB", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onLeft, bc, Modifier.size(80.dp, 56.dp)) { Text("\u25C0", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize) }
            Button(onSoftDrop, bc, Modifier.size(80.dp, 56.dp)) { Text("\u25BC", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize) }
            Button(onRight, bc, Modifier.size(80.dp, 56.dp)) { Text("\u25B6", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize) }
        }
        Button(onHardDrop, bc, Modifier.size(80.dp, 48.dp)) { Text("\u25BC\u25BC", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize) }
    }
}
