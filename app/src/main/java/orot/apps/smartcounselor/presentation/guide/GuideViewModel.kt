package orot.apps.smartcounselor.presentation.guide

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import orot.apps.sognora_viewmodel_extension.scope.onDefault
import orot.apps.sognora_websocket_audio.AudioStreamManager
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GuideViewModel @Inject constructor() : ViewModel(), GuideImpl {
    val currentRenderText: MutableStateFlow<String> = MutableStateFlow("Loading")

    init {
        startGuide()
    }

    override fun startGuide() = onDefault {
        var loadingIndex = 0
        var content = currentRenderText.value
        while (true) {
            delay(700)
            content
                .takeIf { loadingIndex == 3 }
                ?.apply {
                    content = "Loading"
                    loadingIndex = 0
                }
                ?: run {
                    content += "."
                    loadingIndex += 1
                }

            currentRenderText.emit(content)
        }
    }

    override fun connectSocket() {}
}
