package orot.apps.smartcounselor.presentation.ui.screens.conversation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG

@Composable
fun ConversationScreen() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val conversationInfo = mainViewModel.conversationInfo.collectAsState().value
    Log.e(TAG, "ConversationScreen: ${conversationInfo}")

    val isVisible = remember { mainViewModel.conversationVisibleState }

    AnimatedVisibility(
        visibleState = isVisible, enter = fadeIn(
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = Int.MAX_VALUE, easing = LinearOutSlowInEasing),
            targetAlpha = 0.3f
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = conversationInfo.second,
                color = Color.White,
                style = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Center)
            )
        }
    }
}