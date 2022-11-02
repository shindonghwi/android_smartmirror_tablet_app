package orot.apps.smartcounselor

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import orot.apps.smartcounselor.network.model.ChatData
import orot.apps.smartcounselor.presentation.conversation.ConversationType
import orot.apps.sognora_mediaplayer.SognoraTTS
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain
import orot.apps.sognora_websocket_audio.AudioStreamManager
import orot.apps.sognora_websocket_audio.IAudioStreamManager
import orot.apps.sognora_websocket_audio.model.AudioStreamData
import orot.apps.sognora_websocket_audio.model.protocol.MAGO_PROTOCOL
import orot.apps.sognora_websocket_audio.model.protocol.MessageProtocol
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

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
    var audioStreamManager: AudioStreamManager? = null
    val audioState: MutableStateFlow<AudioStreamData<String>> = MutableStateFlow(AudioStreamData.Idle)
    val micIsAvailable = mutableStateOf(false) // 마이크 사용가능 상태

    /** 오디오스트림 생성*/
    fun createAudioStreamManager() {
        if (audioStreamManager != null) {
            audioStreamManager?.stopAudioRecord()
            audioStreamManager = null
        }

        audioStreamManager = AudioStreamManager(object : IAudioStreamManager {
            // 웹소켓(챗서버) 연결: 오디오를 사용가능한 상태로 만들고, 서버에 dm 시작을 알린다.
            override suspend fun connectedWebSocket() {
                coroutineScopeOnDefault(initDelay = 2000) {
                    audioState.update { AudioStreamData.Success() }
                }
            }

            // 웹소켓 해제: 오디오를 사용 불가능한 상태로 만든다.
            override suspend fun disConnectedWebSocket() {
                coroutineScopeOnDefault {
                    audioState.update { AudioStreamData.Closed }
                    changeSendingStateAudioBuffer(false)
                }
            }

            // 웹소켓 연결 실패: 서버가 닫힌 경우
            override suspend fun failedWebSocket() {
                coroutineScopeOnDefault {
                    audioState.update { AudioStreamData.Failed }
                    changeSendingStateAudioBuffer(false)
                }
            }

            // 오디오 버퍼를 서버로 전송한다.
            override suspend fun availableAudioStream() {
                coroutineScopeOnDefault {
                    audioStreamManager?.initAudioRecorder()
                    sendAudioBuffer()
                }
            }

            override suspend fun streamAiTalk(id: String, receivedMsg: MessageProtocol) {
                chatList.add(ChatData(msg = receivedMsg.body?.ment?.text.toString(), isUser = false))
                when (id) {
                    MAGO_PROTOCOL.PROTOCOL_2.id -> {
                        changeSendingStateAudioBuffer(false)
                        changeConversationList(
                            ConversationType.CONVERSATION,
                            listOf(receivedMsg.body?.ment?.text.toString()),
                            receivedMsg
                        )
                    }
                    MAGO_PROTOCOL.PROTOCOL_12.id -> {
                        changeSendingStateAudioBuffer(false)

                        val type = when (receivedMsg.body?.action) {
                            "measurement" -> ConversationType.MEASUREMENT
                            "end" -> ConversationType.END
                            "doctorcall" -> ConversationType.DOCTORCALL
                            else -> ConversationType.CONVERSATION
                        }

                        changeConversationList(
                            type,
                            listOf(receivedMsg.body?.ment?.text.toString()),
                            receivedMsg
                        )
                    }
                    else -> {
                    }
                }
            }

            override suspend fun saidMe(id: String, receivedMsg: MessageProtocol) {
                chatList.add(ChatData(msg = receivedMsg.body?.ment?.text.toString(), isUser = true))
                changeSendingStateAudioBuffer(false)
                changeSaidMeText(receivedMsg.body?.ment?.text.toString())
            }
        })
    }

    fun updateAudioState(state: AudioStreamData<String>) {
        coroutineScopeOnDefault {
            audioState.emit(state)
        }
    }

    fun sendAudioBuffer() = audioStreamManager?.sendAudioRecord()
    fun changeSendingStateAudioBuffer(flag: Boolean) {
        audioStreamManager?.audioSendAvailable = flag
        if (audioState.value is AudioStreamData.Success) {
            micIsAvailable.value = flag
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

    val sognoraTts = SognoraTTS() // tts media player

    fun playGoogleTts(msg: String) {
        sognoraTts.startPlay(msg)
    }

    fun stopGoogleTts() {
        sognoraTts.clear()
    }

    val conversationInfo: MutableStateFlow<Triple<ConversationType, List<String>, MessageProtocol?>> =
        MutableStateFlow(Triple(ConversationType.GUIDE, emptyList(), null))

    fun changeConversationList(type: ConversationType, contentList: List<String>, msgResponse: MessageProtocol?) {
        Log.d("changeConversationList", "changeConversationList: $contentList")
        conversationInfo.value = Triple(type, contentList, msgResponse)
    }

    init {
        Log.d("MAINVIEWMODEL", "init: $this")
    }

    override fun onCleared() {
        Log.d("MAINVIEWMODEL", "onCleared: $this")
        audioState.update { AudioStreamData.Idle }
        audioStreamManager?.stopAudioRecord()
        audioStreamManager = null
        super.onCleared()
    }
}
