package orot.apps.smartcounselor.presentation.guide

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import orot.apps.resources.Gray10
import orot.apps.resources.Gray20
import orot.apps.smartcounselor.presentation.app_style.Display1

@ExperimentalAnimationApi
@Composable
fun GuideScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(), constraintSet = guideScreenConstraintSet()
    ) {
        GuideContent(modifier = Modifier.layoutId("description"))
        LoadingContent(modifier = Modifier.layoutId("loading"))
    }
}

@Composable
private fun guideScreenConstraintSet() = ConstraintSet {
    val description = createRefFor("description")
    val loading = createRefFor("loading")

    constrain(description) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.225f)
    }

    constrain(loading) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.85f)
    }
}

@Composable
private fun GuideContent(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Mago Healthcare",
            style = MaterialTheme.typography.Display1,
            color = Gray10,
            textAlign = TextAlign.Center
        )
        Text(
            "대한민국 대표 음성 의료서비스",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.h3,
            color = Gray10,
            textAlign = TextAlign.Center
        )
    }

//
//    val currentText = guideViewModel.currentRenderText.collectAsState().value
//
//    AnimatedContent(
//        targetState = currentText,
//        transitionSpec = {
//            addAnimation().using(
//                SizeTransform(clip = true)
//            )
//        },
//        contentAlignment = Alignment.Center
//    ) { content ->
//        Text(
//            content,
//            style = MaterialTheme.typography.Display2,
//            color = Primary,
//            textAlign = TextAlign.Center
//        )
//    }
}

@Composable
private fun LoadingContent(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        LoadingText()
    }
}

@Composable
fun LoadingText(guideViewModel: GuideViewModel = hiltViewModel()) {
    guideViewModel.currentRenderText.collectAsState().value.let { content ->
        Text(
            content,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.body1,
            color = Gray20,
            textAlign = TextAlign.Center
        )
    }
}
