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
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.components.LoadingText
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault

@Composable
fun BloodPressureScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = getViewModel(key = "1", vm = hiltViewModel())
) {
    val scaleAnimationIsRunning = remember { mutableStateOf(true) }
    val animVisibleState = remember { MutableTransitionState(false) }.apply {
        coroutineScopeOnDefault {
            delay(1000)
            targetState = true
            delay(4000)
            mainViewModel.updateBottomMenu(BottomMenu.BloodPressure)
            scaleAnimationIsRunning.value = false
        }
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
                        if (scaleAnimationIsRunning.value) {
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

            BloodPressureText(scaleAnimationIsRunning)
        }
    }
}

@Composable
private fun BloodPressureText(scaleAnimationIsRunning: MutableState<Boolean>) {
    if (scaleAnimationIsRunning.value) {
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