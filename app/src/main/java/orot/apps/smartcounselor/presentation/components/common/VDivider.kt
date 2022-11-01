package orot.apps.smartcounselor.presentation.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.presentation.app_style.GrayDivider

@Composable
fun VDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = GrayDivider
    )
}
