package orot.apps.smartcounselor.model.local

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import orot.apps.smartcounselor.model.remote.UserHistoryData

interface IResultMeasurementCardInfo {
    fun getBackgroundColor(): Color
    fun getIconColor(): Color
    fun getTransferStatusEntoKr(): String
}

data class ResultMeasurementCardInfo(
    val type: String,
    val value: Pair<Float, String>, // 80, mg / dL
    val status: String?,
    val image: Painter?,
    val history: UserHistoryData?
) : IResultMeasurementCardInfo {
    override fun getBackgroundColor(): Color {
        return if (status?.contains("warn") == true) {
            Color(0xFFFCE6CC)
        } else if (status?.contains("alert") == true || status?.contains("high") == true) {
            Color(0xFFFCC4C4)
        } else {
            Color(0xFFD6FFDD)
        }
    }

    override fun getIconColor(): Color {
        return if (status?.contains("warn") == true) {
            Color(0xFFE7B638)
        } else if (status?.contains("alert") == true || status?.contains("high") == true) {
            Color(0xFFFD0F00)
        } else {
            Color(0xFF02905D)
        }
    }

    override fun getTransferStatusEntoKr(): String {
        return if (status?.contains("warn") == true) {
            "주의"
        } else if (status?.contains("alert") == true || status?.contains("high") == true) {
            "위험"
        } else {
            "정상"
        }
    }
}
