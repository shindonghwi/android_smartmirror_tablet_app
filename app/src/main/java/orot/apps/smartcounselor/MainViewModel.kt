package orot.apps.smartcounselor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import orot.apps.sognora_viewmodel_extension.scope.onDefault
import orot.apps.sognora_websocket_audio.AudioStreamManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val currentBottomMenu = MutableStateFlow(BottomMenu.Start.type)

    val currentTime: MutableStateFlow<String> = MutableStateFlow(getCurrentTime())
    var audioStreamManager: AudioStreamManager? = null

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
        audioStreamManager = AudioStreamManager()
    }

    fun updateBottomMenu(bottomMenu: BottomMenu) {
        currentBottomMenu.value = bottomMenu.type
    }

}
