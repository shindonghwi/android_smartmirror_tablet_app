package orot.apps.smartcounselor.presentation.components

import androidx.compose.animation.core.*
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.graph.popUpToTop
import orot.apps.smartcounselor.presentation.app_style.*
import orot.apps.smartcounselor.presentation.guide.GuideViewModel
import orot.apps.sognora_compose_extension.animation.clickBounce
import orot.apps.sognora_compose_extension.components.WavesAnimation

@Composable
fun MagoBottomBar(
    navController: NavController, mainViewModel: MainViewModel = hiltViewModel()
) {

    val configuration = LocalConfiguration.current
    val maxHeight = configuration.screenHeightDp * 0.25f


    mainViewModel.currentBottomMenu.collectAsState().value.let { route ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight.dp),
        ) {
            when (route) {
                BottomMenu.Start.type -> {
                    StartBottomBar(navController)
                }
                BottomMenu.Loading.type -> {
                    LoadingBottomBar()
                }
                BottomMenu.Empty.type -> {

                }
                BottomMenu.Conversation.type -> {

                }
                BottomMenu.BloodPressure.type -> {
                    BloodPressureBottomBar()
                }
                BottomMenu.Retry.type -> {
                    RetryBottomBar()
                }
                BottomMenu.RetryAndChat.type -> {
                    RetryAndChatBottomBar()
                }
                BottomMenu.Call.type -> {
                    CallBottomBar()
                }
            }
        }
    }

}


/** 홈(시작하기) 화면 바텀 바 */
@Composable
private fun StartBottomBar(
    navController: NavController, mainViewModel: MainViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier
            .width(startWidth)
            .clickBounce {
                navController.navigate(Screens.Guide.route) {
                    popUpToTop(navController)
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


/** 가이드 화면 바텀 바 */
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
private fun LoadingText(guideViewModel: GuideViewModel = hiltViewModel()) {
    guideViewModel.currentRenderText.collectAsState().value.let { content ->
        Text(
            content,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.body1,
            color = Gray20,
            textAlign = TextAlign.Center
        )
    }
}

/** 권고멘트 화면 바텀 바 */
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

/** 혈압측정 화면 바텀 바 */
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

@Composable
fun RetryBottomBar() {
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
    }
}

@Composable
fun CallBottomBar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
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
            WavesAnimation{
                Icon(
                    painter = painterResource(id = R.drawable.call),
                    "call",
                    tint = Color.White,
                )
            }
        }

    }
}
