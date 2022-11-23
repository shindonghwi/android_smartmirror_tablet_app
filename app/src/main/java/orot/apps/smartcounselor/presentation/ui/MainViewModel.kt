package orot.apps.smartcounselor.presentation.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorder
import mago.apps.sognoraaudio.google_tts.SognoraGoogleTTS
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketListener
import mago.apps.sognorawebsocket.websocket.model.WebSocketState
import okhttp3.Response
import okio.ByteString
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.model.local.ChatData
import orot.apps.smartcounselor.model.local.ConversationType
import orot.apps.smartcounselor.model.remote.HeaderInfo
import orot.apps.smartcounselor.model.remote.MAGO_PROTOCOL
import orot.apps.smartcounselor.model.remote.MessageProtocol
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnMain
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onIO
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val sognoraWebSocket: SognoraWebSocket,
    private val sognoraAudioRecorder: SognoraAudioRecorder,
    val sognoraGoogleTTS: SognoraGoogleTTS
) : ViewModel() {

    /**
     * ================================================
     *     Web Sokcet
     * ================================================
     * */
    fun connectWebSocket() {
        sognoraWebSocket
            .apply {
                setWebSocketListener(object : SognoraWebSocketListener {
                    override fun open(response: Response) {}
                    override fun onMessageText(msg: String) {
                        Log.w(TAG, "onMessageText: $msg", )
                    }
                    override fun onMessageByteString(byteString: ByteString) {}
                    override fun fail(response: Response?, t: Throwable) {}
                    override fun close(code: Int, reason: String) {}
                    override fun onClosing(code: Int, reason: String) {}
                })
            }
            .run {
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

    fun updateBottomMenu(bottomMenu: BottomMenu) {
        coroutineScopeOnMain {
            currentBottomMenu.value = bottomMenu.type
        }
    }

    var heartAnimationState = mutableStateOf(true)

    fun updateHeartAnimationState(flag: Boolean) {
        heartAnimationState.value = flag
    }


    /**
     * ================================================
     *     Audio Stream
     * ================================================
     * */
//    var audioStreamManager: AudioStreamManager? = null
    fun getWebSocketState() = sognoraWebSocket.getWebSocketState()

    //    val webSocketState: MutableStateFlow<WebSocketState> = MutableStateFlow(WebSocketState.Idle)
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
        Log.d(TAG, "sendProtocol: protocol: $protocolId / body: $body")

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
//        audioStreamManager = AudioStreamManager()
//
//        audioStreamManager?.run {
//            initWebSocket(object : AudioStreamManageable {
//                override suspend fun stateWebSocket(state: WebSocketState) {
//
//                    var initDelay = 0L
//
//                    when (state) {
//                        is WebSocketState.Idle -> {}
//                        is WebSocketState.Connected -> {
//                            initDelay = 2000
//
//                            if (audioStreamManager?.audioRecord == null) {
//                                audioStreamManager?.initAudioRecorder()
//                                sendAudioBuffer()
//                            }
//
//                        }
//                        is WebSocketState.DisConnected -> {}
//                        is WebSocketState.Closing -> {}
//                        is WebSocketState.Failed -> {}
//                    }
//
//                    coroutineScopeOnDefault(initDelay = initDelay) {
//                        updateWebSocketState(state)
//                    }
//                }
//
//                override suspend fun stateAudioStream(isAvailable: Boolean) {
//                }
//
//                override suspend fun receivedMessageString(msg: String) {
//                    try {
//                        val receivedMsg: MessageProtocol =
//                            Gson().fromJson(msg, MessageProtocol::class.java)
//
//                        val isUser = receivedMsg.header.protocol_id == MAGO_PROTOCOL.PROTOCOL_11.id
//                        receivedMsg.body?.ment?.text?.run {
//                            if (this.isNotEmpty()) {
//                                chatList.add(ChatData(msg = this, isUser = isUser))
//                            }
//                        }
//
//                        when (receivedMsg.header.protocol_id) {
//                            MAGO_PROTOCOL.PROTOCOL_2.id -> { // 클라이언트 연결 요청 후 응답 ACK
//                                changeSendingStateAudioBuffer(false)
//                                changeConversationList(
//                                    ConversationType.CONVERSATION,
//                                    listOf(receivedMsg.body?.ment?.text.toString()),
//                                    receivedMsg
//                                )
//                            }
//                            MAGO_PROTOCOL.PROTOCOL_4.id -> { // 클라이언트 UTTERANCE 요청 후 응답 ACK -> audio stream start
//
//                            }
//                            MAGO_PROTOCOL.PROTOCOL_11.id -> {
//                                changeSendingStateAudioBuffer(false)
//                                changeSaidMeText(receivedMsg.body?.ment?.text.toString())
//                            }
//                            MAGO_PROTOCOL.PROTOCOL_12.id -> { // -> audio stream start
//                                sendProtocol(13, conversationInfo.value.third)
//                                changeSendingStateAudioBuffer(false)
//
//                                var type = when (receivedMsg.body?.action) {
//                                    "measurement" -> ConversationType.MEASUREMENT
//                                    "end" -> ConversationType.END
//                                    "doctorcall" -> ConversationType.DOCTORCALL
//                                    "exit" -> ConversationType.EXIT
//                                    else -> ConversationType.CONVERSATION
//                                }
//
//                                changeConversationList(
//                                    type,
//                                    listOf(receivedMsg.body?.ment?.text.toString()),
//                                    receivedMsg
//                                )
//                            }
//                            else -> {}
//                        }
//
//                    } catch (e: Exception) {
//                    }
//                }
//
//                override suspend fun receivedMessageByteString(byte: ByteString) {
//                }
//
//            })
//        }
    }

    //    fun updateWebSocketState(state: WebSocketState) = webSocketState.update { state }
//
//    fun sendAudioBuffer() = audioStreamManager?.sendAudioRecord()
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

//    val sognoraTts = SognoraTTS() // tts media player

    fun playGoogleTts(msg: String) {
//        sognoraTts.startPlay(msg)
    }

    fun stopGoogleTts() {
//        sognoraTts.clear()
    }

    val conversationInfo: MutableStateFlow<Triple<ConversationType, List<String>, MessageProtocol?>> =
        MutableStateFlow(Triple(ConversationType.GUIDE, listOf(""), null))

    fun changeConversationList(
        type: ConversationType, contentList: List<String>, msgResponse: MessageProtocol?
    ) {
        Log.d("changeConversationList", "changeConversationList: $contentList")
        conversationInfo.value = Triple(type, contentList, msgResponse)
    }

    init {
        Log.d("MAINVIEWMODEL", "init: $this")
    }

//    fun clear() {
//        updateBottomMenu(BottomMenu.Start)
//        updateWebSocketState(WebSocketState.Idle)
//        audioStreamManager?.stopAudioRecord()
//        audioStreamManager = null
//    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MAINVIEWMODEL", "onCleared: $this")
//        clear()
    }
}