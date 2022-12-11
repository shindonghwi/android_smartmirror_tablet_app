package orot.apps.smartcounselor.model.local

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

interface IResultMeasurementCardInfo {
    fun getBackgroundColor(): Color
    fun getIconColor(): Color
}

data class ResultMeasurementCardInfo(
    val type: String,
    val value: Pair<Float, String>, // 80, mg / dL
    val ment: String,
    val image: Painter?,
) : IResultMeasurementCardInfo {
    override fun getBackgroundColor(): Color {
        return if (ment.contains("주의")) {
            Color(0xFFFCE6CC)
        } else if (ment.contains("위험")) {
            Color(0xFFFCC4C4)
        } else {
            Color(0xFFD6FFDD)
        }
    }

    override fun getIconColor(): Color {
        return if (ment.contains("주의")) {
            Color(0xFFE7B638)
        } else if (ment.contains("위험")) {
            Color(0xFFFF5151)
        } else {
            Color(0xFF478F96)
        }
    }
}
