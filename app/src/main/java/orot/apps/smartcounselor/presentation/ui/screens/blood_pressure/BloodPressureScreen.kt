package orot.apps.smartcounselor.presentation.ui.screens.blood_pressure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.components.bottombar.LoadingText
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnDefault

@Composable
fun BloodPressureScreen() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val animVisibleState = remember { MutableTransitionState(false) }.apply {

        DisposableEffect(key1 = Unit, effect = {
            val animationJob = coroutineScopeOnDefault {
                delay(1000)
                targetState = true
            }

            onDispose {
                animationJob.cancel()
            }
        })
    }

    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.2f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        )
    )

    AnimatedVisibility(
        visibleState = animVisibleState, enter = fadeIn(
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = 2500, easing = LinearOutSlowInEasing)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .scale(
                        if (mainViewModel.heartAnimationState.value) {
                            scale
                        } else {
                            1f
                        }
                    )
                    .size(300.dp),
                painter = painterResource(id = R.drawable.heartbeat),
                contentDescription = null,
                tint = Color.Unspecified
            )

            BloodPressureText()
        }
    }
}

@Composable
private fun BloodPressureText() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    if (mainViewModel.heartAnimationState.value) {
        LoadingText(
            defaultContent = "혈압을 측정중입니다", textStyle = MaterialTheme.typography.subtitle1
        )
    } else {
        Text(
            text = "혈압 측정이 완료되었습니다",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.subtitle1,
            color = Color.White
        )
    }

}