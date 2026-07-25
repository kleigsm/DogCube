package com.dogcube.game.model

import androidx.compose.ui.graphics.Color
import com.dogcube.game.ui.theme.*

enum class DogBreed(
    val displayName: String,
    val color: Color,
    val emoji: String
) {
    DACHSHUND("腊肠犬", ColorDachshund, "\uD83D\uDC15"),
    CORGI("柯基", ColorCorgi, "\uD83D\uDC36"),
    FRENCHIE("法斗", ColorFrenchie, "\uD83D\uDC3E"),
    SHIBA("柴犬", ColorShiba, "\uD83E\uDD8A"),
    HUSKY("哈士奇", ColorHusky, "\uD83D\uDC3A"),
    GOLDEN("金毛", ColorGolden, "\uD83D\uDC29"),
    BORDER_COLLIE("边牧", ColorBorderCollie, "\uD83D\uDC15\u200D\uD83E\uDDBA")
}
