package orot.apps.smartcounselor.presentation.guide

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Display1
import orot.apps.smartcounselor.presentation.app_style.Gray10
import orot.apps.smartcounselor.presentation.conversation.ConversationType
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_websocket_audio.model.AudioStreamData

@ExperimentalAnimationApi
@Composable
fun GuideScreen(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    WebSocketState()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.createAudioStreamManager() // 가이드 화면 진입시 소켓 연결
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(), constraintSet = guideScreenConstraintSet()
    ) {
        GuideContent(modifier = Modifier.layoutId("description"))
    }
}

@Composable
fun WebSocketState(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val state = mainViewModel.audioState.collectAsState().value

    if (state is AudioStreamData.Success) {
        LaunchedEffect(key1 = Unit) {
            mainViewModel.changeConversationList(
                ConversationType.GUIDE,
                listOf(
                    "안녕하세요"
//                            "\n\nMago Healthcare 서비스에 오신걸 환영합니다\n\nAI가 먼저 대화를 시작합니다. \n\n잠시만 기다려주세요",
                ),
                null
            )
            navigationKit.clearAndMove(Screens.Conversation.route) {
                mainViewModel.updateBottomMenu(BottomMenu.Conversation)
            }
        }
    } else if (state is AudioStreamData.Failed) {
        navigationKit.clearAndMove(Screens.ServerConnectionFailScreen.route) {
            mainViewModel.updateBottomMenu(BottomMenu.ServerRetry)
        }
    }
}

@Composable
private fun guideScreenConstraintSet() = ConstraintSet {
    val description = createRefFor("description")

    constrain(description) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.225f)
    }
}

@Composable
private fun GuideContent(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Mago Healthcare",
            style = MaterialTheme.typography.Display1,
            color = Gray10,
            textAlign = TextAlign.Center
        )
        Text(
            "대한민국 대표 음성 의료서비스",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.h3,
            color = Gray10,
            textAlign = TextAlign.Center
        )
    }
}
