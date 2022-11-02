package orot.apps.smartcounselor.presentation.blood_pressure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.components.LoadingText
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault

@Composable
fun BloodPressureScreen(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
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
                    .size(350.dp),
                painter = painterResource(id = R.drawable.heartbeat),
                contentDescription = null,
                tint = Color.Unspecified
            )

            BloodPressureText()
        }
    }
}

@Composable
private fun BloodPressureText(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
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