package orot.apps.smartcounselor.presentation.components.bottombar.blood_pressure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.style.Green80
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.modifier.noDuplicationClickable
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnDefault

@Composable
fun WaitingMeasurement() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(top = 30.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeartAnimationIcon()
            WatchAndChairIcon()
        }
        ManualInput()
    }
}


@Composable
private fun HeartAnimationIcon() {
    val animVisibleState = remember { MutableTransitionState(true) }
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.2f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        )
    )

    AnimatedVisibility(
        visibleState = animVisibleState,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing)
        )
    ) {
        Icon(
            modifier = Modifier
                .scale(if (animVisibleState.targetState) scale else 1f)
                .size(150.dp),
            painter = painterResource(id = R.drawable.result_heartbeat),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}


@Composable
private fun WatchAndChairIcon() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WatchIcon()
        ChairIcon()
    }
}


@Composable
private fun WatchIcon() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val watch = mainViewModel.medicalDeviceWatchData.collectAsState().value
    val isWatchDataIsExist = watch.isDataExist()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .size(200.dp),
            painter = painterResource(id = R.drawable.watch),
            contentDescription = null,
            tint = if (isWatchDataIsExist) Green80 else  Color.White.copy(0.8f)
        )
        Text(
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 30.dp),
            text = if (isWatchDataIsExist) "데이터 수신 완료" else "데이터 수신 준비완료",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            color = if (isWatchDataIsExist) Green80 else Color.White.copy(0.8f)
        )
    }

}

@Composable
private fun ChairIcon() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val chair = mainViewModel.medicalDeviceChairData.collectAsState().value
    val isChairDataIsExist = chair.isDataExist()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .size(200.dp),
            painter = painterResource(id = R.drawable.chair),
            contentDescription = null,
            tint = if (isChairDataIsExist) Green80 else  Color.White.copy(0.8f)
        )
        Text(
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 30.dp),
            text = if (isChairDataIsExist) "데이터 수신 완료" else "데이터 수신 준비완료",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            color = if (isChairDataIsExist) Green80 else Color.White.copy(0.8f)
        )
    }
}

@Composable
private fun ManualInput() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value

    val inputButtonState = remember { MutableTransitionState(false) }.apply {
        DisposableEffect(key1 = Unit, effect = {
            val animationJob = coroutineScopeOnDefault {
                delay(10000)
                targetState = true
            }

            onDispose {
                animationJob.cancel()
            }
        })
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        AnimatedVisibility(
            visibleState = inputButtonState, enter = fadeIn(
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            ), exit = fadeOut(
                animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 40.dp)
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                    .background(Primary)
                    .noDuplicationClickable {
                        mainViewModel.updateMedicalEndStatus(true)
                    }
                    .padding(all = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "건강정보를 더 자세하게 입력하기",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    color = Color.White
                )
            }
        }
    }
}
