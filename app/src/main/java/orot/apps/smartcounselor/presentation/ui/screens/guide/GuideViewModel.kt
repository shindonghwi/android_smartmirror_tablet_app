package orot.apps.smartcounselor.presentation.ui.screens.guide

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onDefault

class GuideViewModel : ViewModel() {
    val currentRenderText: MutableStateFlow<String> = MutableStateFlow("")

    init {
        startGuide()
    }

    private fun startGuide() = onDefault {
        var loadingIndex = 0
        var content = currentRenderText.value
        while (true) {
            delay(700)
            content.takeIf { loadingIndex == 3 }?.apply {
                content = ""
                loadingIndex = 0
            } ?: run {
                content += "."
                loadingIndex += 1
            }

            currentRenderText.emit(content)
        }
    }
}
