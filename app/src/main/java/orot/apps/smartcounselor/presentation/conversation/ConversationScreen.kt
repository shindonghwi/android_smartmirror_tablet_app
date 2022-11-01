package orot.apps.smartcounselor.presentation.conversation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.sognora_compose_extension.components.AnimationTTSText
import orot.apps.sognora_compose_extension.components.IAnimationTextCallback
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_websocket_audio.model.protocol.MAGO_PROTOCOL

@Composable
fun ConversationScreen(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val conversationInfo = mainViewModel.conversationInfo.collectAsState().value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimationTTSText(
            modifier = Modifier,
            textList = conversationInfo.second,
            iAnimationTextCallback = object : IAnimationTextCallback {
                override suspend fun startAnimation() {
                    when (conversationInfo.first) {
                        ConversationType.CONVERSATION -> {
                            when (conversationInfo.third?.header?.protocol_id) {
                                MAGO_PROTOCOL.PROTOCOL_2.id -> { // 김철수님 안녕하세요 ~
                                    mainViewModel.audioStreamManager?.sendProtocol(
                                        3,
                                        conversationInfo.third
                                    ) // APP_UTTERANCE_START_REG
                                }
                            }
                        }
                        else -> {}
                    }
                }

                override suspend fun endAnimation() {
                    mainViewModel.changeSendingStateAudioBuffer(true)
                    when (conversationInfo.first) {
                        ConversationType.GUIDE -> {
                            mainViewModel.audioStreamManager?.sendProtocol(1) // APP_DIALOG_START_REG
                        }
                        ConversationType.CONVERSATION -> {
                            when (conversationInfo.third?.header?.protocol_id) {
                                MAGO_PROTOCOL.PROTOCOL_4.id -> { // 김철수님 안녕하세요 ~
                                    mainViewModel.changeSendingStateAudioBuffer(true)
                                }
                                MAGO_PROTOCOL.PROTOCOL_12.id -> { // AI의 질문이 끝남 ~
                                    mainViewModel.changeSendingStateAudioBuffer(true)
                                }
                            }
                        }

                        else -> {}
                    }
                }

            }
        ) { content ->
            LaunchedEffect(key1 = conversationInfo) {
                mainViewModel.run {
                    try {
                        playGoogleTts(conversationInfo.second.filter { it == content }[0])
                    } catch (e: java.lang.Exception) {
                        Log.d("ASdasdas", "ConversationScreen: $e")
                    }
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