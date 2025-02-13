package orot.apps.smartcounselor.presentation.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.presentation.style.GrayDivider

@Composable
fun VDivider(color: Color = GrayDivider) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = color
    )
}
