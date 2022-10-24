package orot.apps.smartcounselor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import orot.apps.smartcounselor.network.service.TtsService
import orot.apps.smartcounselor.network.service.ttsService
import orot.apps.sognora_mediaplayer.SognoraMediaPlayer
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import orot.apps.sognora_viewmodel_extension.scope.onDefault
import orot.apps.sognora_websocket_audio.AudioStreamData
import orot.apps.sognora_websocket_audio.AudioStreamManager
import orot.apps.sognora_websocket_audio.AudioStreamManagerImpl
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val sognoraMediaPlayer = SognoraMediaPlayer()
    val currentBottomMenu = mutableStateOf(BottomMenu.Start.type)
    val currentTime: MutableStateFlow<String> = MutableStateFlow(getCurrentTime())
    var audioStreamManager: AudioStreamManager? = null
    val receiveMsg: MutableStateFlow<AudioStreamData<String>> =
        MutableStateFlow(AudioStreamData.WebSocketDisConnected)


    init {
        setCurrentTime()
    }

    private fun setCurrentTime() = onDefault {
        while (true) {
            currentTime.emit(getCurrentTime())
            delay(1000L)
        }
    }

    private fun getCurrentTime() =
        SimpleDateFormat("yyyy.MM.dd-HH:mm-EE", Locale.KOREA).format(Date())

    fun createAudioStreamManager() {
        if (audioStreamManager == null) {
            coroutineScopeOnDefault {
                delay(5000)
                receiveMsg.emit(AudioStreamData.WebSocketConnected)
            }
            audioStreamManager = AudioStreamManager(object : AudioStreamManagerImpl {
                override suspend fun connectedWebSocket() {
                    receiveMsg.update { AudioStreamData.WebSocketConnected }
                }

                override suspend fun disConnectedWebSocket() {
                    receiveMsg.update { AudioStreamData.WebSocketDisConnected }
                }

                override suspend fun receivedMsg(audioStreamData: AudioStreamData.ReceivedData<String>) {
                    receiveMsg.update { AudioStreamData.ReceivedData(audioStreamData.msg) }
                }

            })
        }
    }

    fun updateBottomMenu(bottomMenu: BottomMenu) {
        currentBottomMenu.value = bottomMenu.type
    }

    var ttsJob: Job? = null
    fun fetchTtsMessage(msg: String) {
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

    override fun onCleared() {
        super.onCleared()
        audioStreamManager?.run {
            stopAudioRecord()
            null
        }
        ttsJob?.cancel()
    }
}
