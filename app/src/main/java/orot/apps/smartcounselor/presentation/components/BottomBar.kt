package orot.apps.smartcounselor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.*
import orot.apps.smartcounselor.presentation.guide.GuideViewModel
import orot.apps.sognora_compose_extension.animation.clickBounce

@Composable
fun MagoBottomBar(navController: NavController) {

    val configuration = LocalConfiguration.current
    val maxHeight = configuration.screenHeightDp * 0.25f


    navController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight.dp),
        ) {
            when (route) {
                Screens.Home.route -> {
                    HomeBottomBar(navController)
                }
                Screens.Guide.route -> {
                    GuideBottomBar()
                }
            }
        }
    }
}

/** 홈(시작하기) 화면 바텀 바 */
@Composable
private fun HomeBottomBar(navController: NavController) {
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .width(startWidth)
                .clickBounce { navController.navigate(Screens.Guide.route) }
                .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
                .background(Primary)
                .padding(vertical = 36.dp),
            textAlign = TextAlign.Center,
            text = "시작",
            style = MaterialTheme.typography.Display1,
            color = White
        )
    }
}


/** 가이드 화면 바텀 바 */
@Composable
private fun GuideBottomBar(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (mainViewModel.audioStreamManager == null) {
            mainViewModel.createAudioStreamManager()
        }
    }

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