package orot.apps.smartcounselor.presentation.components.bottombar

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.presentation.components.animation.RotationAnimation
import orot.apps.smartcounselor.presentation.components.animation.WavesAnimation
import orot.apps.smartcounselor.presentation.components.common.VDivider
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component.BloodPressureSubmitButton
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component.InputBloodPressure
import orot.apps.smartcounselor.presentation.ui.screens.guide.GuideViewModel
import orot.apps.smartcounselor.presentation.ui.screens.home.component.AgeTextField
import orot.apps.smartcounselor.presentation.ui.screens.home.component.SexRadioButton
import orot.apps.smartcounselor.presentation.ui.screens.home.component.StartButton
import orot.apps.smartcounselor.presentation.ui.utils.modifier.clickBounce
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.getViewModel

@Composable
fun MagoBottomBar() {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current

    mainViewModel.currentBottomMenu.value.let { route ->
        val maxHeight = configuration.screenHeightDp * getBottomBarHeight(route)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(maxHeight.dp),
        ) {
            when (route) {
                BottomMenu.Start.type -> {
                    VDivider()
                    BloodPressureBottomBar()
//                    StartBottomBar()
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
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "회원님의 정보를 입력해주세요",
            color = Color.White,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeTextField()
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
    guideViewModel: GuideViewModel = getViewModel(GuideViewModel())
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

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {
                    mainViewModel.reset()
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 20.dp, horizontal = 35.dp),
            style = MaterialTheme.typography.h2,
            color = Color.Black,
            textAlign = TextAlign.Center)
        Text("대화내역",
            modifier = Modifier
                .clickBounce {
                    mainViewModel.moveScreen(Screens.ChatList, BottomMenu.Retry)
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFCFCF))
                .padding(vertical = 20.dp, horizontal = 35.dp),
            style = MaterialTheme.typography.h2,
            color = Color.Black,
            textAlign = TextAlign.Center)
    }
}

/** 혈압측정 바텀 바 */
@Composable
private fun BloodPressureBottomBar() {
    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputBloodPressure(modifier = Modifier.weight(0.8f))
        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
        ){
            BloodPressureSubmitButton()
        }
    }
}

/** 다시하기 */
@Composable
fun RetryBottomBar() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {
                    mainViewModel.reset()
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 20.dp, horizontal = 35.dp),
            style = MaterialTheme.typography.h2,
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
fun ConversationBottomBar() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
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
private fun SaidMeText() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val text = mainViewModel.saidMeText.collectAsState().value
    Text(
        modifier = Modifier.padding(start = 60.dp),
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun ServerRetryBottomBar() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current
    val startWidth: Dp = configuration.screenWidthDp.dp * 0.35f

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(modifier = Modifier
            .width(startWidth)
            .clickBounce {
                mainViewModel.reset()
            }
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 36.dp),
            textAlign = TextAlign.Center,
            text = "재연결",
            style = MaterialTheme.typography.Display2,
            color = Color.White)
    }
}

fun getBottomBarHeight(route: String): Float {
    return when (route) {
        BottomMenu.Start.type -> 0.7f
        BottomMenu.Loading.type -> 0.33f
        BottomMenu.Empty.type -> 0f
        BottomMenu.Conversation.type -> 0.33f
        BottomMenu.BloodPressure.type -> 0.7f
        BottomMenu.Retry.type -> 0.33f
        BottomMenu.RetryAndChat.type -> 0.33f
        BottomMenu.Call.type -> 0.33f
        BottomMenu.ServerRetry.type -> 0.33f
        else -> 0f
    }
}