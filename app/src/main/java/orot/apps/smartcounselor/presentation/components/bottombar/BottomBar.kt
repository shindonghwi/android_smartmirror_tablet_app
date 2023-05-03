package orot.apps.smartcounselor.presentation.components.bottombar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import orot.apps.smartcounselor.BuildConfig
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.BuildShowMode
import orot.apps.smartcounselor.presentation.components.animation.WavesAnimation
import orot.apps.smartcounselor.presentation.components.bottombar.blood_pressure.WaitingMeasurement
import orot.apps.smartcounselor.presentation.components.bottombar.home.UserRadioButton
import orot.apps.smartcounselor.presentation.components.common.VDivider
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.GrayDivider
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component.BloodPressureSubmitButton
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component.InputBloodPressure
import orot.apps.smartcounselor.presentation.ui.screens.guide.GuideViewModel
import orot.apps.smartcounselor.presentation.ui.utils.modifier.clickBounce
import orot.apps.smartcounselor.presentation.ui.utils.modifier.noDuplicationClickable
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
                    StartBottomBar()
                }
                BottomMenu.Loading.type -> {
                    LoadingBottomBar()
                }
                BottomMenu.Empty.type -> {

                }
                BottomMenu.Conversation.type -> {
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

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.3f }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserRadioButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(startWidth)
                    .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                    .background(Primary)
                    .noDuplicationClickable {
                        if (mainViewModel.selectedUser != null) {
                            mainViewModel.run {
                                if (BuildConfig.SHOW_MODE == BuildShowMode.RECOMMENDATION.value) {
                                    moveScreen(null, BottomMenu.BloodPressure)
                                } else {
                                    connectWebSocket()
                                    moveScreen(Screens.Guide, BottomMenu.Loading)
                                }
                            }
                        } else {
                            Toast
                                .makeText(context, "사용자를 선택해주세요", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 18.dp, horizontal = 30.dp),
                    text = "입장하기",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(startWidth)
                    .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                    .background(Color.White)
                    .noDuplicationClickable {
                        mainViewModel.changeAccountBottomSheetFlag(true)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 18.dp, horizontal = 30.dp),
                    text = "계정 연결하기",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    color = Primary
                )
            }
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
                    mainViewModel.run {
                        changeChatViewShowing(true)
                        moveScreen(Screens.ChatList, BottomMenu.Retry)
                    }
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
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val isEndMeasurement = mainViewModel.isEndMedicalMeasurement.collectAsState().value

    if (isEndMeasurement || BuildConfig.SHOW_MODE == BuildShowMode.RECOMMENDATION.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputBloodPressure(modifier = Modifier.weight(0.8f))
            Box(
                modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center
            ) {
                BloodPressureSubmitButton()
            }
        }
    } else {
        WaitingMeasurement()
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

/** 대화하기 */
@Composable
fun ConversationBottomBar() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val micIsAvailable = remember { mainViewModel.isAvailableAudioBuffer }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                VDivider(color = GrayDivider.copy(0.4f))
            }

            WavesAnimation(
                waveSize = 80.dp,
                waveDuration = 2500,
                waveColor = Color.White.copy(alpha = if (micIsAvailable.value) 0.4f else 0.01f),
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp),
                        painter = painterResource(R.drawable.mic),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            if (micIsAvailable.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 1.dp
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SaidMeText()
        }
    }
}

@Composable
private fun SaidMeText() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val text = mainViewModel.saidMeText.collectAsState().value
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.h1,
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
        BottomMenu.Start.type -> 0.35f
        BottomMenu.Loading.type -> 0.33f
        BottomMenu.Empty.type -> 0f
        BottomMenu.Conversation.type -> 0.4f
        BottomMenu.BloodPressure.type -> 0.7f
        BottomMenu.Retry.type -> 0.33f
        BottomMenu.RetryAndChat.type -> 0.33f
        BottomMenu.Call.type -> 0.33f
        BottomMenu.ServerRetry.type -> 0.33f
        else -> 0f
    }
}