package orot.apps.smartcounselor.presentation.ui.utils.modifier

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.borderVGradient(
    width: Dp = 2.dp,
    colorList: List<Color>,
    shape: RoundedCornerShape = CircleShape
) = composed {
    this.border(
        width = width, brush = Brush.verticalGradient(colors = colorList), shape = shape
    )
}
