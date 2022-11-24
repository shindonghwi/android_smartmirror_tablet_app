package orot.apps.smartcounselor.presentation.ui

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorder
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketListener
import mago.apps.sognorawebsocket.websocket.model.WebSocketState
import okhttp3.Response
import okio.ByteString
import orot.apps.smartcounselor.graph.NavigationKit
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.ActionType
import orot.apps.smartcounselor.model.local.ChatData
import orot.apps.smartcounselor.model.remote.*
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onDefault
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onIO
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onMain
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val sognoraWebSocket: SognoraWebSocket,
    val sognoraAudioRecorder: SognoraAudioRecorder,
) : ViewModel() {

    lateinit var navigationKit: NavigationKit

    /**
     * ================================================
     *     Web Sokcet
     * ================================================
     * */
    fun connectWebSocket() {
        sognoraWebSocket.apply {
            setWebSocketListener(object : SognoraWebSocketListener {
                override fun open(response: Response) {
                    moveScreen(Screens.Conversation, BottomMenu.Conversation)
                    startAudioRecorder()
                    sendProtocol(1)
                }

                override fun onMessageText(msg: String) {

                    try {
                        val receivedMsg: MessageProtocol =
                            Gson().fromJson(msg, MessageProtocol::class.java)

                        val protocol = receivedMsg.header.protocol_id
                        val content = receivedMsg.body?.ment?.text

                        val isUser = protocol == MAGO_PROTOCOL.PROTOCOL_11.id

                        if (!content.isNullOrEmpty()) {
                            chatList.add(ChatData(msg = content.toString(), isUser = isUser))
                        }

                        Log.w(TAG, "onMessageText: $receivedMsg")


                        when (protocol) {
                            /** AI의 첫 인사 */
                            MAGO_PROTOCOL.PROTOCOL_2.id -> {
                                playGoogleTts(content.toString())
                                changeConversationList(
                                    ActionType.GREETING_END, content.toString(), receivedMsg
                                )
                            }

                            /** 내가 말한 내용을 STT 해서 결과를 응답받음. */
                            MAGO_PROTOCOL.PROTOCOL_11.id -> {
                                stopGoogleTts()
                                changeSendingStateAudioBuffer(false)
                                changeSaidMeText(content.toString())
                            }

                            /** AI의 다음 질문 */
                            MAGO_PROTOCOL.PROTOCOL_12.id -> {
                                val type = when (receivedMsg.body?.action) {
                                    "measurement" -> ActionType.MEASUREMENT
                                    "end" -> ActionType.END
                                    "doctorcall" -> ActionType.DOCTORCALL
                                    "exit" -> ActionType.EXIT
                                    else -> ActionType.CONVERSATION
                                }
                                playGoogleTts(content.toString())
                                changeConversationList(type, content.toString(), receivedMsg)

                                if (conversationInfo.value.first == ActionType.DOCTORCALL) {
                                    changeSaidMeText("")
                                    moveScreen(null, BottomMenu.Conversation)
                                }
                                if (type == ActionType.END) {

                                }
                            }
                            MAGO_PROTOCOL.PROTOCOL_16.id -> {
                                changeSaidMeText("")
                            }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "onMessageText Error: ${e.message}")
                    }
                }

                override fun onMessageByteString(byteString: ByteString) {}
                override fun fail(response: Response?, t: Throwable) {}
                override fun close(code: Int, reason: String) {}
                override fun onClosing(code: Int, reason: String) {}
            })
        }.run {
            initWebSocket("ws://172.30.1.15:8080/ws/chat")
        }
    }

    private var isAvailableAudioBuffer: Boolean = false
    fun startAudioRecorder() {
        sognoraAudioRecorder.startAudioRecorder()
        onIO {
            try {
                do {
                    if (isAvailableAudioBuffer) {
                        val bufferSize = sognoraAudioRecorder.getMinBuffer()
                        val bufferInfo = sognoraAudioRecorder.frameBuffer(bufferSize)
                        if (bufferInfo.second < -1) break
                        sognoraWebSocket.sendBuffer(bufferInfo.first, bufferSize)
                    }
                } while (true)
            } catch (e: Exception) {
                Log.d(TAG, "sendAudioRecord ERROR: ${e.message}")
                sognoraAudioRecorder.stopAudioRecorder()
            }
        }
    }

    /**
     * ================================================
     *     SAVE DATA
     * ================================================
     * */
    var userAge: Int = 0 // Default: 0세
    var userSex: Boolean = true // Default: 남
    var bloodPressureMax: Int = 0 // Default: 최고혈압
    var bloodPressureMin: Int = 0 // Default: 최저혈압
    var bloodPressureSugar: Int = 0 // Default: 혈당량

    /**
     * ================================================
     *     UI STATE
     * ================================================
     * */
    val currentBottomMenu = mutableStateOf(BottomMenu.Start.type) // 바텀 메뉴

    var heartAnimationState = mutableStateOf(true)

    fun updateHeartAnimationState(flag: Boolean) {
        heartAnimationState.value = flag
    }

    fun moveScreen(screen: Screens? = null, bottomMenu: BottomMenu? = null) {
        screen?.let {
            navigationKit.clearAndMove(it.route) {
                bottomMenu?.run { currentBottomMenu.value = this.type }
            }
        } ?: run {
            bottomMenu?.run { currentBottomMenu.value = this.type }
        }

    }

    /**
     * ================================================
     *     Audio Stream
     * ================================================
     * */
    fun getWebSocketState() = sognoraWebSocket.getWebSocketState()
    val micIsAvailable = mutableStateOf(false) // 마이크 사용가능 상태


    /** 웹 소켓으로 이벤트 전달하기 */
    var lastProtocolNum: Int = -1
    fun sendProtocol(protocolNum: Int, body: MessageProtocol? = null) {
        if (lastProtocolNum == protocolNum) return
        else lastProtocolNum = protocolNum

        var protocolId = ""

        when (protocolNum) {
            1 -> protocolId = MAGO_PROTOCOL.PROTOCOL_1.id
            2 -> protocolId = MAGO_PROTOCOL.PROTOCOL_2.id
            3 -> protocolId = MAGO_PROTOCOL.PROTOCOL_3.id
            4 -> protocolId = MAGO_PROTOCOL.PROTOCOL_4.id
            5 -> protocolId = MAGO_PROTOCOL.PROTOCOL_5.id
            6 -> protocolId = MAGO_PROTOCOL.PROTOCOL_6.id
            7 -> protocolId = MAGO_PROTOCOL.PROTOCOL_7.id
            8 -> protocolId = MAGO_PROTOCOL.PROTOCOL_8.id
            9 -> protocolId = MAGO_PROTOCOL.PROTOCOL_9.id
            10 -> protocolId = MAGO_PROTOCOL.PROTOCOL_10.id
            11 -> protocolId = MAGO_PROTOCOL.PROTOCOL_11.id
            12 -> protocolId = MAGO_PROTOCOL.PROTOCOL_12.id
            13 -> protocolId = MAGO_PROTOCOL.PROTOCOL_13.id
            14 -> protocolId = MAGO_PROTOCOL.PROTOCOL_14.id
            15 -> protocolId = MAGO_PROTOCOL.PROTOCOL_15.id
            16 -> protocolId = MAGO_PROTOCOL.PROTOCOL_16.id
            17 -> protocolId = MAGO_PROTOCOL.PROTOCOL_17.id
            18 -> protocolId = MAGO_PROTOCOL.PROTOCOL_18.id
        }
        Log.w(TAG, "sendProtocol: protocol: $protocolId / body: $body")

        val msg = if (body == null) {
            MessageProtocol(header = HeaderInfo(protocol_id = protocolId), body = null)
        } else {
            MessageProtocol(header = HeaderInfo(protocol_id = protocolId), body = body.body)
        }

        val params = Gson().toJson(msg)
        sognoraWebSocket.sendMsg(params)
    }

    /** 오디오스트림 생성*/
    fun createAudioStreamManager() {
        connectWebSocket()
    }

    fun changeSendingStateAudioBuffer(flag: Boolean) {
        isAvailableAudioBuffer = flag
        if (getWebSocketState().value is WebSocketState.Connected) {
            micIsAvailable.value = flag
            if (flag) {
                sendProtocol(3, conversationInfo.value.third)
            }
        }
    }

    val saidMeText = MutableStateFlow("")
    val chatList = arrayListOf<ChatData>()
    fun changeSaidMeText(msg: String) {
        saidMeText.value = msg
    }

    /**
     * ================================================
     *     TTS
     * ================================================
     * */

    enum class TTSCallback {
        IDLE, START, DONE, ERROR
    }

    private val params = Bundle()
    var conversationVisibleState = MutableTransitionState(false)
    var tts: TextToSpeech? = null
    val ttsState = MutableStateFlow(TTSCallback.IDLE)
    fun initTTS(context: Context) {
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null)
        tts = TextToSpeech(context) { state ->
            if (state == TextToSpeech.SUCCESS) {
                tts?.apply {
                    language = Locale.KOREAN
                    setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            changeSendingStateAudioBuffer(false)
                            conversationVisibleState.targetState = true
                            ttsState.value = TTSCallback.START
                        }

                        override fun onDone(utteranceId: String?) {
                            conversationVisibleState.targetState = false
                            ttsState.value = TTSCallback.DONE
                            changeSendingStateAudioBuffer(true)

                            when (conversationInfo.value.first) {
                                ActionType.MEASUREMENT -> {
                                    moveScreen(Screens.BloodPressure, BottomMenu.BloodPressure)
                                }
                                ActionType.RESULT_WAITING -> {
                                    sendProtocol(
                                        15, MessageProtocol(
                                            header = HeaderInfo(protocol_id = MAGO_PROTOCOL.PROTOCOL_15.id),
                                            body = BodyInfo(
                                                measurement = MeasurementInfo(
                                                    blood_pressure = listOf(
                                                        bloodPressureMax, bloodPressureMin
                                                    ),
                                                    blood_sugar = bloodPressureSugar,
                                                ),
                                                user = UserInfo(
                                                    gender = if (userSex) "M" else "F",
                                                    age = userAge
                                                )
                                            )
                                        )
                                    )
                                }
                                ActionType.EXIT -> {
                                    onDefault {
                                        changeSendingStateAudioBuffer(false)
                                        moveScreen(bottomMenu = BottomMenu.Loading)
                                        delay(2000)
                                        moveScreen(bottomMenu = BottomMenu.RetryAndChat)
                                    }
                                }
                                ActionType.END -> {
                                    if (conversationInfo.value.third?.body?.ment?.uri?.contains("doctorcall") == true){
                                        val content = "상담은 잘 진행되셨나요?"
                                        onDefault {
                                            moveScreen(bottomMenu = BottomMenu.Loading)
                                            delay(2000)
                                            moveScreen(bottomMenu = BottomMenu.Call)
                                            delay(1000)
                                            onMain {
                                                Toast.makeText(
                                                    context, "상담원으로부터 전화가 왔습니다", Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            delay(3000)
                                            onMain {
                                                Toast.makeText(
                                                    context, "상담원과의 전화가 종료되었습니다", Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            moveScreen(bottomMenu = BottomMenu.Loading)
                                            delay(1000)
                                            changeSaidMeText("")
                                            moveScreen(bottomMenu = BottomMenu.Conversation)
                                            playGoogleTts(content)
                                            changeConversationList(
                                                ActionType.MANUAL_DOCTORCALL_END,
                                                content,
                                                conversationInfo.value.third
                                            )
                                        }
                                    }

                                    // 아직 학습중이여서 답변을 못함
                                    else if (conversationInfo.value.third?.body?.ment?.uri?.contains("measurement_learning") == true){
                                        onDefault {
                                            moveScreen(bottomMenu = BottomMenu.RetryAndChat)
                                        }
                                    }
                                }
                                ActionType.MANUAL_DOCTORCALL_END -> {

                                }
                                else -> {}
                            }
                        }

                        override fun onError(utteranceId: String?) {
                            ttsState.value = TTSCallback.ERROR
                        }
                    })
                }
            }
        }
    }

    fun playGoogleTts(msg: String) {
        tts?.let {
            if (it.isSpeaking) {
                it.stop()
            }
            it.speak(msg, TextToSpeech.QUEUE_ADD, params, msg)
        }
    }

    fun stopGoogleTts() {
        tts?.run {
            stop()
            null
        }
    }

    val conversationInfo: MutableStateFlow<Triple<ActionType, String, MessageProtocol?>> =
        MutableStateFlow(Triple(ActionType.IDLE, "", null))

    fun changeConversationList(
        type: ActionType, contentList: String, msgResponse: MessageProtocol?
    ) {
        Log.d(TAG, "changeConversationList: $contentList")
        conversationInfo.value = Triple(type, contentList, msgResponse)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MAINVIEWMODEL", "onCleared: $this")
    }
}