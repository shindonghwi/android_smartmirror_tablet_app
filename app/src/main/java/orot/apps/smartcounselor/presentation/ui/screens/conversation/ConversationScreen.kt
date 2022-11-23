package orot.apps.smartcounselor.presentation.ui.screens.conversation

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
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
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.ActionType
import orot.apps.smartcounselor.presentation.components.animation.ITextAnimationTTSCallback
import orot.apps.smartcounselor.presentation.components.animation.TextAnimationTTS
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.navigationKit

@Composable
fun ConversationScreen() {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val conversationInfo = mainViewModel.conversationInfo.collectAsState().value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TextAnimationTTS(
            modifier = Modifier,
            text = conversationInfo.second,
            exitTransition = if (conversationInfo.first.name == ActionType.END.name) {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = Int.MAX_VALUE, easing = FastOutSlowInEasing
                    ),
                )
            } else {
                fadeOut(
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                )
            },
            iTextAnimationTTSCallback = object : ITextAnimationTTSCallback {
                override suspend fun startAnimation() {
                    Log.e("ConversationScreen", "startAnimation: ${conversationInfo}")
//                    when (conversationInfo.first) {
//                        ConversationType.CONVERSATION -> {}
//                        ConversationType.DOCTORCALL -> {
//                            mainViewModel.run {
//                                changeSaidMeText("")
//                                updateBottomMenu(BottomMenu.Conversation)
//                            }
//                        }
//                        ConversationType.EXIT -> {
//                            mainViewModel.run {
//                                sendProtocol(17) // 종료
//                            }
//                        }
//                        else -> {}
//                    }
                }

                override suspend fun endAnimation() {
                    Log.e("ConversationScreen", "endAnimation: ${conversationInfo}")
                    when (conversationInfo.first) {
                        ActionType.GREETING_END -> { // AI의 첫 인사 끝
                            mainViewModel.changeSendingStateAudioBuffer(true)
                        }
                        ActionType.MEASUREMENT -> { // 혈압 측정화면으로 넘어가기
                            navigationKit.clearAndMove(Screens.BloodPressure.route) {
                                mainViewModel.updateBottomMenu(BottomMenu.BloodPressure)
                            }
                        }
                        else -> {}
                    }
//                        ConversationType.IDLE -> {
//                            when (conversationInfo.third?.header?.protocol_id) {
//                                MAGO_PROTOCOL.PROTOCOL_2.id -> { // 김철수님 안녕하세요 ~
//                                    mainViewModel.changeSendingStateAudioBuffer(true)
//                                }
//                            }
//                        }
//                        ConversationType.CONVERSATION -> {
//                            when (conversationInfo.third?.header?.protocol_id) {
////                                MAGO_PROTOCOL.PROTOCOL_2.id -> { // 김철수님 안녕하세요 ~
////                                    mainViewModel.changeSendingStateAudioBuffer(true)
////                                }
//                                MAGO_PROTOCOL.PROTOCOL_12.id -> { // AI의 질문이 끝남 ~
//                                    mainViewModel.changeSendingStateAudioBuffer(true)
//                                }
//                            }
//                        }
//
//                        ConversationType.MEASUREMENT -> {
//                            when (conversationInfo.third?.header?.protocol_id) {
//                                MAGO_PROTOCOL.PROTOCOL_12.id -> {
//                                    coroutineScopeOnMain {
//                                        MagoActivity.navigationKit.clearAndMove(Screens.BloodPressure.route) {
//                                            mainViewModel.updateBottomMenu(BottomMenu.BloodPressure)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        ConversationType.RESULT_WAITING -> {
//                            mainViewModel.run {
//                                sendProtocol(
//                                    15, MessageProtocol(
//                                        header = HeaderInfo(protocol_id = MAGO_PROTOCOL.PROTOCOL_15.id),
//                                        body = BodyInfo(
//                                            null,
//                                            null,
//                                            null,
//                                            null,
//                                            null,
//                                            null,
//                                            measurement = MeasurementInfo(
//                                                blood_pressure = listOf(
//                                                    bloodPressureMax, bloodPressureMin
//                                                ),
//                                                blood_sugar = bloodPressureSugar,
//                                            ),
//                                            user = UserInfo(
//                                                gender = if (mainViewModel.userSex) "M" else "F",
//                                                age = mainViewModel.userAge
//                                            )
//                                        )
//                                    )
//                                )
//                            }
//                        }
//                        ConversationType.END -> {
//                            coroutineScopeOnMain {
//                                if (conversationInfo.third?.body?.ment?.uri?.contains("doctorcall") == true) {
//                                    mainViewModel.run {
//                                        coroutineScopeOnDefault {
//                                            updateBottomMenu(BottomMenu.Call)
//                                            delay(1000)
//                                            coroutineScopeOnMain {
//                                                Toast.makeText(
//                                                    context, "상담원으로부터 전화가 왔습니다", Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
//                                            delay(5000)
//                                            coroutineScopeOnMain {
//                                                Toast.makeText(
//                                                    context, "상담원과의 전화가 종료되었습니다", Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
//                                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
//                                            delay(1000)
//                                            mainViewModel.run {
//                                                changeSaidMeText("")
//                                                updateBottomMenu(BottomMenu.Conversation)
//                                                changeConversationList(
//                                                    ConversationType.MANUAL_OPERATION,
//                                                    listOf("상담은 잘 진행되셨나요?"),
//                                                    conversationInfo.third
//                                                )
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    MagoActivity.navigationKit.clearAndMove(Screens.Home.route) {
//                                        mainViewModel.updateBottomMenu(BottomMenu.RetryAndChat)
//                                    }
//                                }
//                            }
//                        }
//                        ConversationType.DOCTORCALL -> {
//                            mainViewModel.changeSendingStateAudioBuffer(true)
//                        }
//                        ConversationType.EXIT -> {
//                            coroutineScopeOnDefault {
//                                mainViewModel.changeSendingStateAudioBuffer(false)
//                                mainViewModel.updateBottomMenu(BottomMenu.Loading)
//                                delay(2000)
//                                mainViewModel.updateBottomMenu(BottomMenu.RetryAndChat)
//                            }
//                        }
//                        ConversationType.MANUAL_OPERATION -> {
//                            mainViewModel.changeSendingStateAudioBuffer(true)
//                        }
//                    }
                }
            },
        ) { content ->
            Log.w("ConversationScreen", "ConversationScreen: $content")
            LaunchedEffect(key1 = conversationInfo) {
                mainViewModel.run {
                    try {
                        playGoogleTts(conversationInfo.second)
                    } catch (e: Exception) {
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
