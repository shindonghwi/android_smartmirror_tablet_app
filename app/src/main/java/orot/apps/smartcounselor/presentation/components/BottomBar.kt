package orot.apps.smartcounselor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import orot.apps.smartcounselor.presentation.guide.GuideViewModel
import orot.apps.sognora_compose_extension.animation.clickBounce
import orot.apps.sognora_compose_extension.components.RotationAnimation
import orot.apps.sognora_compose_extension.components.WavesAnimation
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun MagoBottomBar(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val maxHeight = configuration.screenHeightDp * 0.2f

    mainViewModel.currentBottomMenu.value.let { route ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
            }
        }
    }
}

@Composable
private fun VDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp), color = GrayDivider
    )
}


/** 홈(시작하기) 화면 바텀 바 */
@Composable
private fun StartBottomBar(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier
            .width(startWidth)
            .clickBounce {
                navigationKit.clearAndMove(Screens.Guide.route) {
                    mainViewModel.updateBottomMenu(BottomMenu.Loading)
                }
            }
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 36.dp),
            textAlign = TextAlign.Center,
            text = "시작",
            style = MaterialTheme.typography.Display1,
            color = White)
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
    guideViewModel: GuideViewModel = hiltViewModel()
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
private fun RetryAndChatBottomBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {

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
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "혈압측정 결과는",
            style = MaterialTheme.typography.h2,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            "126",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(vertical = 8.dp, horizontal = 12.dp),
            style = MaterialTheme.typography.h1,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Text(
            "입니다",
            style = MaterialTheme.typography.h2,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/** 다시하기 */
@Composable
fun RetryBottomBar(
    mainViewModel: MainViewModel = hiltViewModel()
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
                        mainViewModel.updateBottomMenu(BottomMenu.Empty)
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

//        AnimationText(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 50.dp),
//            initDelay = 1000,
//            enterTransition = fadeIn(),
//            isEnded = isEnded,
//            exitTransition = fadeOut()
//        ) {
//            Text(
//                "",
//                color = Color.White,
//                style = MaterialTheme.typography.Display3.copy(textAlign = TextAlign.Center)
//            )
//        }
    }
}
