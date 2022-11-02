package orot.apps.smartcounselor.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.*
import orot.apps.smartcounselor.presentation.components.blood_pressure.BloodPressureSubmitButton
import orot.apps.smartcounselor.presentation.components.blood_pressure.InputBloodPressure
import orot.apps.smartcounselor.presentation.components.common.VDivider
import orot.apps.smartcounselor.presentation.components.home.AgeTextField
import orot.apps.smartcounselor.presentation.components.home.SexRadioButton
import orot.apps.smartcounselor.presentation.components.home.StartButton
import orot.apps.smartcounselor.presentation.guide.GuideViewModel
import orot.apps.sognora_compose_extension.animation.clickBounce
import orot.apps.sognora_compose_extension.components.RotationAnimation
import orot.apps.sognora_compose_extension.components.WavesAnimation
import orot.apps.sognora_viewmodel_extension.clearAndNewVMS
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_websocket_audio.model.AudioStreamData

@Composable
fun MagoBottomBar(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val maxHeight = configuration.screenHeightDp * 0.23f

    mainViewModel.currentBottomMenu.value.let { route ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(maxHeight.dp),
        ) {
            when (route) {
                BottomMenu.Start.type -> {
                    StartBottomBar()
                }
                BottomMenu.Loading.type -> {
                    LoadingBottomBar()
                }
                BottomMenu.Empty.type -> {

                }
                BottomMenu.Conversation.type -> {
                    VDivider()
                    ConversationBottomBar()
                }
                BottomMenu.BloodPressure.type -> {
                    VDivider()
                    BloodPressureBottomBar()
                }
                BottomMenu.Retry.type -> {
                    VDivider()
                    RetryBottomBar()
                }
                BottomMenu.RetryAndChat.type -> {
                    VDivider()
                    RetryAndChatBottomBar()
                }
                BottomMenu.Call.type -> {
                    CallBottomBar()
                }
                BottomMenu.ServerRetry.type -> {
                    ServerRetryBottomBar()
                }
            }
        }
    }
}


/** 홈(시작하기) 화면 바텀 바 */
@Composable
private fun StartBottomBar() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.Start
            ) {
                Text("회원님의 정보를 입력해주세요", color = Color.White, style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.height(8.dp))
                AgeTextField()
                Spacer(modifier = Modifier.height(8.dp))
                SexRadioButton()
            }
            StartButton()
        }
    }
}

/** 로딩 바텀 바 */
@Composable
private fun LoadingBottomBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        LoadingText()
    }
}

@Composable
fun LoadingText(
    defaultContent: String = "Loading",
    textStyle: TextStyle = MaterialTheme.typography.body1,
    guideViewModel: GuideViewModel = getViewModel(vm = hiltViewModel())
) {
    guideViewModel.currentRenderText.collectAsState().value.let { dot ->
        Text(
            "${defaultContent}$dot",
            modifier = Modifier.padding(top = 16.dp),
            style = textStyle,
            color = Gray20,
            textAlign = TextAlign.Center
        )
    }
}

/** 권고멘트 바텀 바 */
@Composable
private fun RetryAndChatBottomBar(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {
                    navigationKit.clearAndMove(Screens.Home.route) {
                        mainViewModel.updateBottomMenu(BottomMenu.Start)
                        clearAndNewVMS()
                    }
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
        Text("대화내역",
            modifier = Modifier
                .clickBounce {
                    navigationKit.clearAndMove(Screens.ChatList.route) {
                        mainViewModel.updateBottomMenu(BottomMenu.Retry)
                    }
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFCFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
    }
}

/** 혈압측정 바텀 바 */
@Composable
private fun BloodPressureBottomBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputBloodPressure()
        BloodPressureSubmitButton()
    }
}

/** 다시하기 */
@Composable
fun RetryBottomBar(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {
                    navigationKit.clearAndMove(Screens.Home.route) {
                        mainViewModel.updateBottomMenu(BottomMenu.Start)
                        clearAndNewVMS()
                    }
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
    }
}

/** 상담원 전화 걸려올때 */
@Composable
fun CallBottomBar() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.padding(all = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "상담원",
                modifier = Modifier.padding(vertical = 35.dp),
                style = MaterialTheme.typography.h3,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            WavesAnimation {
                Icon(
                    painter = painterResource(id = R.drawable.call),
                    "call",
                    tint = Color.White,
                )
            }
        }
    }
}

/** 상담원 전화 걸려올때 */
@Composable
fun ConversationBottomBar(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val isPlaying = remember { mainViewModel.micIsAvailable }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RotationAnimation(
            modifier = Modifier.padding(start = 60.dp),
            isPlaying = isPlaying,
            iconDrawable = R.drawable.mago_logo_icon,
            iconSize = 80.dp
        )

        SaidMeText()
    }
}

@Composable
private fun SaidMeText(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val text = mainViewModel.saidMeText.collectAsState().value
    Text(
        modifier = Modifier.padding(start = 60.dp),
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.Display3.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun ServerRetryBottomBar(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(modifier = Modifier
            .width(startWidth)
            .clickBounce {
                mainViewModel.updateAudioState(AudioStreamData.Idle)
                navigationKit.clearAndMove(Screens.Guide.route) {
                    mainViewModel.updateBottomMenu(BottomMenu.Loading)
                }
            }
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 36.dp),
            textAlign = TextAlign.Center,
            text = "재연결",
            style = MaterialTheme.typography.Display1,
            color = Color.White)
    }
}

