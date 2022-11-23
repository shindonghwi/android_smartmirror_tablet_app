package orot.apps.smartcounselor.presentation.components.animation

import android.speech.tts.UtteranceProgressListener
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
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnDefault
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnMain

interface ITextAnimationTTSCallback {
    suspend fun startAnimation()
    suspend fun endAnimation()
}

@Composable
fun TextAnimationTTS(
    modifier: Modifier = Modifier,
    termDuration: Long = 300,
    enterTransition: EnterTransition = fadeIn(
        animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing),
    ),
    exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    ),
    textList: List<String>,
    useSpeakMode: Boolean = true,
    iTextAnimationTTSCallback: ITextAnimationTTSCallback? = null,
    content: @Composable (String) -> Unit,
) {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel
    var currentIdx = remember { 0 }
    var currentText = textList.first()

    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        targetState = true
    }

    DisposableEffect(key1 = currentText) {
        coroutineScopeOnDefault {
            iTextAnimationTTSCallback?.startAnimation()
            mainViewModel.sognoraGoogleTTS.createTts(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    coroutineScopeOnMain {
                        animVisibleState.targetState = true
                    }
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
                                mainViewModel.sognoraGoogleTTS.startPlay(currentText)
                            }
                        } ?: kotlin.run {
                            iTextAnimationTTSCallback?.endAnimation()
                        }
                    }
                }

                override fun onError(utteranceId: String?) {}
            }).run {
                coroutineScopeOnDefault {
                    delay(termDuration)
                    if (useSpeakMode) {
                        mainViewModel.sognoraGoogleTTS.startPlay(currentText)
                    }
                }
            }

        }

        onDispose {
            mainViewModel.sognoraGoogleTTS.stop()
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