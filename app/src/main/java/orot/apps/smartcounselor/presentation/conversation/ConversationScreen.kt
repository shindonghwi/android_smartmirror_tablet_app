package orot.apps.smartcounselor.presentation.conversation

import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.sognora_compose_extension.components.AnimationTTSText
import orot.apps.sognora_compose_extension.components.IAnimationTextCallback
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain
import orot.apps.sognora_websocket_audio.model.protocol.*

@Composable
fun ConversationScreen() {

    val context = LocalContext.current
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
                                if (conversationInfo.third?.body?.ment?.id.toString().contains("doctorcall")) {
                                    coroutineScopeOnDefault {
                                        updateBottomMenu(BottomMenu.Call)
                                        delay(1000)
                                        coroutineScopeOnMain {
                                            Toast.makeText(context, "상담원으로부터 전화가 왔습니다", Toast.LENGTH_SHORT).show()
                                        }
                                        delay(5000)
                                        coroutineScopeOnMain {
                                            Toast.makeText(context, "상담원과의 전화가 종료되었습니다", Toast.LENGTH_SHORT).show()
                                        }
                                        mainViewModel.updateBottomMenu(BottomMenu.Loading)
                                        delay(1000)
                                        mainViewModel.run {
                                            changeSaidMeText("")
                                            updateBottomMenu(BottomMenu.Conversation)
                                            delay(1000)
                                            changeSendingStateAudioBuffer(true)
                                        }
                                    }

                                } else {
                                    updateBottomMenu(BottomMenu.RetryAndChat)
                                }
                            }
                        }
                        ConversationType.DOCTORCALL -> {
                            mainViewModel.run {
                                changeSaidMeText("")
                                updateBottomMenu(BottomMenu.Conversation)
                            }
                        }
                        ConversationType.EXIT -> {
                            mainViewModel.run {
                                audioStreamManager?.sendProtocol(17) // 종료
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
                        ConversationType.EXIT -> {
                            coroutineScopeOnDefault {
                                mainViewModel.changeSendingStateAudioBuffer(false)
                                mainViewModel.updateBottomMenu(BottomMenu.Loading)
                                delay(3000)
                                mainViewModel.updateBottomMenu(BottomMenu.RetryAndChat)
                            }
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
