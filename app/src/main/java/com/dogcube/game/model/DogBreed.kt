package com.dogcube.game.model


import androidx.compose.ui.graphics.Color
import com.dogcube.game.ui.theme.*


enum class DogBreed(
    val displayName: String,
    val color: Color,
    val emoji: String
) {
    DACHSHUND("À°³¦È®", ColorDachshund, "??"),
    CORGI("¿Â»ù", ColorCorgi, "??"),
    FRENCHIE("·¨¶·", ColorFrenchie, "??"),
    SHIBA("²ñÈ®", ColorShiba, "??"),
    HUSKY("¹þÊ¿Ææ", ColorHusky, "??"),
    GOLDEN("½ðÃ«", ColorGolden, "??"),
    BORDER_COLLIE("±ßÄÁ", ColorBorderCollie, "?????")
}
