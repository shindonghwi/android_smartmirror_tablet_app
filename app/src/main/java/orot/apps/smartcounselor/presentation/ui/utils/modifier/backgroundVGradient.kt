package orot.apps.smartcounselor.presentation.ui.utils.modifier

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.backgroundVGradient(
    colorList: List<Color>,
) = composed {
    this.background(brush = Brush.verticalGradient(colors = colorList))
}

fun Modifier.backgroundHGradient(
    colorList: List<Color>,
) = composed {
    this.background(brush = Brush.horizontalGradient(colors = colorList))
}
