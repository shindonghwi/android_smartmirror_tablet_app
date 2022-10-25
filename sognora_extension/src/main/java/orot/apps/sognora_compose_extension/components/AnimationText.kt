package orot.apps.sognora_compose_extension.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AnimationText(
    modifier: Modifier = Modifier,
    enterDuration: Int = 1500,
    exitDuration: Int = 1000,
    termDuration: Long = 1000,
    enterTransition: EnterTransition = fadeIn(
        animationSpec = tween(durationMillis = enterDuration, easing = FastOutLinearInEasing),
    ),
    exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(durationMillis = exitDuration, easing = FastOutSlowInEasing),
    ),
    textList: List<String>,
    content: @Composable (String) -> Unit,
) {
    val currentText = remember { mutableStateOf(textList.first()) }

    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        CoroutineScope(Dispatchers.Default).launch {
            repeat(textList.size) {
                delay(enterDuration.toLong())
                targetState = true
                currentText.value = textList[it]
                delay(enterDuration.toLong() + exitDuration.toLong() + termDuration)
                targetState = false
            }
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = animVisibleState,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        content(currentText.value)
    }

}