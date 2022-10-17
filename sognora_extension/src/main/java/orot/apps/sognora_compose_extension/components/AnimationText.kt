package orot.apps.sognora_compose_extension.components

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AnimationText(
    modifier: Modifier = Modifier,
    initDelay: Long,
    enterDuration: Int = 2000,
    exitDuration: Int = 1000,
    enterTransition: EnterTransition = slideInVertically(
        animationSpec = tween(durationMillis = enterDuration, easing = LinearOutSlowInEasing),
        initialOffsetY = { offset -> -offset / 4 }
    ),
    exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(durationMillis = exitDuration, easing = FastOutSlowInEasing),
    ),
    content: @Composable () -> Unit
) {
    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        CoroutineScope(Dispatchers.Default).launch {
            takeIf { initDelay != 0L }?.run {
                delay(initDelay)
                targetState = true
            }
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = animVisibleState,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        content()
    }

}