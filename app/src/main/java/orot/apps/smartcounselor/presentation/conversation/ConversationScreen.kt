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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.sognora_compose_extension.components.AnimationTTSText
import orot.apps.sognora_compose_extension.components.IAnimationTextCallback
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain
import orot.apps.sognora_websocket_audio.model.protocol.*

@Composable
fun ConversationScreen() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel
    val conversationInfo = mainViewModel.conversationInfo.collectAsState().value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimationTTSText(modifier = Modifier,
            textList = conversationInfo.second,
            iAnimationTextCallback = object : IAnimationTextCallback {
                override suspend fun startAnimation() {
                    Log.d("ASdsadasd", "startAnimation: $conversationInfo")
                    when (conversationInfo.first) {
                        ConversationType.END -> {
                            mainViewModel.run {
                                updateBottomMenu(BottomMenu.RetryAndChat)
                            }
                        }
                        ConversationType.DOCTORCALL -> {
                            mainViewModel.run {
                                saidMeText.value = ""
                                updateBottomMenu(BottomMenu.Conversation)
                            }
                        }
                        else -> {}
                    }
                }

                override suspend fun endAnimation() {
                    Log.d("endAnimation", "endAnimation: $conversationInfo")
                    when (conversationInfo.first) {
                        ConversationType.GUIDE -> {
                            mainViewModel.run {
                                audioStreamManager?.sendProtocol(1) // APP_DIALOG_START_REG
                            }
                        }
                        ConversationType.CONVERSATION -> {
                            when (conversationInfo.third?.header?.protocol_id) {
                                MAGO_PROTOCOL.PROTOCOL_2.id -> { // 김철수님 안녕하세요 ~
                                    mainViewModel.changeSendingStateAudioBuffer(true)
                                }
                                MAGO_PROTOCOL.PROTOCOL_12.id -> { // AI의 질문이 끝남 ~
                                    mainViewModel.changeSendingStateAudioBuffer(true)
                                }
                            }
                        }

                        ConversationType.MEASUREMENT -> {
                            when (conversationInfo.third?.header?.protocol_id) {
                                MAGO_PROTOCOL.PROTOCOL_12.id -> {
                                    coroutineScopeOnMain {
                                        navigationKit.clearAndMove(Screens.BloodPressure.route) {
                                            mainViewModel.updateBottomMenu(BottomMenu.BloodPressure)
                                        }
                                    }
                                }
                            }
                        }
                        ConversationType.RESULT_WAITING -> {
                            mainViewModel.run {
                                audioStreamManager?.sendProtocol(
                                    15, MessageProtocol(
                                        header = HeaderInfo(protocol_id = MAGO_PROTOCOL.PROTOCOL_15.id),
                                        body = BodyInfo(
                                            null, null, null, null, null, null,
                                            measurement = MeasurementInfo(
                                                blood_pressure = listOf(bloodPressureMax, bloodPressureMin),
                                                blood_sugar = bloodPressureSugar,
                                            ),
                                            user = UserInfo(
                                                gender = if (mainViewModel.userSex) "M" else "F",
                                                age = mainViewModel.userAge
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        ConversationType.END -> {
                            Log.d("Asadsasd", "endAnimation: END")
                        }
                        ConversationType.DOCTORCALL -> {
                            mainViewModel.changeSendingStateAudioBuffer(true)
                        }
                    }
                }
            }) { content ->
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
