package orot.apps.smartcounselor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orot.apps.smartcounselor.network.service.TtsService
import orot.apps.smartcounselor.network.service.ttsService
import orot.apps.sognora_mediaplayer.SognoraMediaPlayer
import orot.apps.sognora_mediaplayer.SognoraTTS
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain
import orot.apps.sognora_websocket_audio.AudioStreamManager
import orot.apps.sognora_websocket_audio.IAudioStreamManager
import orot.apps.sognora_websocket_audio.model.AudioStreamData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    /**
     * ================================================
     *     UI STATE
     * ================================================
     * */
    val currentBottomMenu = mutableStateOf(BottomMenu.Start.type) // 바텀 메뉴

    fun updateBottomMenu(bottomMenu: BottomMenu) {
        currentBottomMenu.value = bottomMenu.type
    }

    /**
     * ================================================
     *     Audio Stream
     * ================================================
     * */
    var audioStreamManager: AudioStreamManager? = null
    val audioState: MutableStateFlow<AudioStreamData<String>> = MutableStateFlow(AudioStreamData.UnAvailable)
    val micIsAvailable = mutableStateOf(false) // 마이크 사용가능 상태

    /** 오디오스트림 생성*/
    fun createAudioStreamManager() {
        audioStreamManager?.run {
            stopAudioRecord()
            null
        }

        if (audioStreamManager == null) {
            audioStreamManager = AudioStreamManager(object : IAudioStreamManager {
                // 웹소켓(챗서버) 연결: 오디오를 사용가능한 상태로 만들고, 서버에 dm 시작을 알린다.
                override suspend fun connectedWebSocket() {
                    coroutineScopeOnDefault(initDelay = 2000) {
                        audioState.update { AudioStreamData.Available() }
                    }
                }

                // 웹소켓 해제: 오디오를 사용 불가능한 상태로 만든다.
                override suspend fun disConnectedWebSocket() {
                    coroutineScopeOnDefault {
                        audioState.update { AudioStreamData.UnAvailable }
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

                override suspend fun streamAiTalk() {
                }
            })
        }
    }

    fun sendAudioBuffer() = audioStreamManager?.sendAudioRecord()
    fun changeSendingStateAudioBuffer(flag: Boolean) {
        if (audioState.value is AudioStreamData.Available) {
            micIsAvailable.value = flag
            audioStreamManager?.audioSendAvailable = flag
        }
    }

    /**
     * ================================================
     *     TTS
     * ================================================
     * */

    val sognoraMediaPlayer = SognoraMediaPlayer() // tts media player
    val sognoraTts = SognoraTTS() // tts media player
    var ttsJob: Job? = null
    fun fetchMediaPlayerTtsMessage(msg: String) {
        ttsJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        ttsJob = coroutineScopeOnIO {
            val response = ttsService.getConvertTts(msg = msg)
            sognoraMediaPlayer.playAudio("${TtsService.BASE_URL}/audio/${response.body()?.id}")
        }
    }

    fun playMediaPlayerTts(url: String) {
        ttsJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        ttsJob = coroutineScopeOnMain {
            sognoraMediaPlayer.playAudio(url)
        }
    }

    fun playGoogleTts(msg: String){
        sognoraTts.startPlay(msg)
    }

    fun stopGoogleTts(){
        sognoraTts.clear()
    }

    var guideMsgList = listOf(
        "안녕하세요",
        "Mago Healthcare 서비스에 오신걸 환영합니다",
        "AI의 대화를 기다려주세요"
    )
    var guideTtsList = arrayListOf<Pair<String, String>>()

//    suspend fun setGuideTtsUrlList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            guideMsgList.forEach {
////                val response = async { ttsService.getConvertTts(msg = it) }
//                val item = Pair(it, "${TtsService.BASE_URL}/audio/${response.await().body()?.id}")
//                if (!guideTtsList.contains(item)) {
//                    guideTtsList.add(item)
//                }
//            }
//        }
//    }


    override fun onCleared() {
        super.onCleared()
        audioStreamManager?.run {
            stopAudioRecord()
            null
        }
        ttsJob?.cancel()
    }
}
