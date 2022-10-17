package orot.apps.smartcounselor.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor() : ViewModel(), GuideImpl {

    private val guideTextList: List<String> = listOf(
        "안녕하세요 고객님",
        "Mago 헬스케어 AI를 준비중이에요",
        "잠시만 기다려주세요",
    )
    private var currentRenderIndex: Int = 0
    val currentRenderText: MutableStateFlow<String> = MutableStateFlow(guideTextList[currentRenderIndex])

    init {
        viewModelScope.launch {
            startGuide()
        }
    }

    override suspend fun startGuide() = withContext(Dispatchers.Default) {
        while (true) {
            delay(1000)
            currentRenderIndex += 1
            if (currentRenderIndex == guideTextList.size) {
                currentRenderIndex = 0
            }
            currentRenderText.emit(guideTextList[currentRenderIndex])
        }
    }

    override suspend fun connectSocket() {
    }
}
