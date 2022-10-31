package orot.apps.smartcounselor.presentation.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.sognora_compose_extension.components.AnimationText
import orot.apps.sognora_compose_extension.components.IAnimationTextCallback
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault

@Composable
fun ConversationScreen(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimationText(
            modifier = Modifier,
            textList = mainViewModel.guideMsgList,
            iAnimationTextCallback = object : IAnimationTextCallback {
                override suspend fun startAnimation() {}
                override suspend fun endAnimation() {
                    coroutineScopeOnDefault {
                        mainViewModel.run {
                            delay(1000)
                            updateRotating(true)
                            changeSendingStateAudioBuffer(true)
                        }
                    }
                }
            }
        ) { content ->
            LaunchedEffect(key1 = Unit) {
                mainViewModel.run {
                    playTts(guideTtsList.filter { it.first == content }[0].second)
                }
            }

            Text(
                text = content,
                color = Color.White,
                style = MaterialTheme.typography.Display2.copy(textAlign = TextAlign.Center)
            )
        }
    }

}