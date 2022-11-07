package orot.apps.sognora_compose_extension.components

import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import orot.apps.sognora_mediaplayer.SognoraTTS
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain
import kotlin.concurrent.timer

interface IAnimationTextCallback {
    suspend fun startAnimation()
    suspend fun endAnimation()
}

@Composable
fun AnimationTTSText(
    modifier: Modifier = Modifier,
    termDuration: Long = 1000,
    enterTransition: EnterTransition = fadeIn(
        animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing),
    ),
    exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    ),
    textList: List<String>,
    useSpeakMode: Boolean = true,
    iAnimationTextCallback: IAnimationTextCallback? = null,
    content: @Composable (String) -> Unit,
) {
    val context = LocalContext.current
    var currentIdx = remember { 0 }
    var currentText = textList.first()

    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        targetState = true
    }

    DisposableEffect(key1 = currentText) {
        val sognoraTTS = SognoraTTS()

        coroutineScopeOnDefault {
            iAnimationTextCallback?.startAnimation()
            sognoraTTS.createTts(context, object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    coroutineScopeOnMain {
                        animVisibleState.targetState = true
                    }
//                    timer(initialDelay = textList[currentIdx].length * 250).toLong())
//                    {
//                        second++
//                        Log.d("Asdasdasd", "second: ${second}")
//                    }
                }

                override fun onDone(utteranceId: String?) {
                    coroutineScopeOnDefault {
                        coroutineScopeOnMain {
                            animVisibleState.targetState = false
                        }
                        delay(termDuration)
                        currentIdx += 1
                        textList.elementAtOrNull(currentIdx)?.let {
                            currentText = it
                            if (useSpeakMode) {
                                sognoraTTS.startPlay(currentText)
                            }
                        } ?: kotlin.run {
                            iAnimationTextCallback?.endAnimation()
                        }
                    }
                }

                override fun onError(utteranceId: String?) {}
            }).run {
                coroutineScopeOnDefault {
                    delay(termDuration)
                    if (useSpeakMode) {
                        sognoraTTS.startPlay(currentText)
                    }
                }
            }

        }

        onDispose {
            sognoraTTS.clear()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visibleState = animVisibleState,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        content(currentText)
    }

}