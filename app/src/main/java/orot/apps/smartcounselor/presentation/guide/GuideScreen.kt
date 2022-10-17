package orot.apps.smartcounselor.presentation.guide

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import orot.apps.resources.Primary
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.smartcounselor.presentation.components.MagoAppBar
import orot.apps.sognora_compose_extension.animation.addAnimation
import orot.apps.sognora_compose_extension.components.AnimationText

@ExperimentalAnimationApi
@Composable
fun GuideScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize(), constraintSet = guideScreenConstraintSet()
    ) {
        Column(
            modifier = Modifier.layoutId("description"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            GuideContent()
        }
    }
}

@Composable
private fun guideScreenConstraintSet() = ConstraintSet {
    val description = createRefFor("description")

    constrain(description) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.225f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun GuideContent(guideViewModel: GuideViewModel = hiltViewModel()) {
    val currentText = guideViewModel.currentRenderText.collectAsState().value

    AnimatedContent(
        targetState = currentText,
        transitionSpec = {
            addAnimation().using(
                SizeTransform(clip = true)
            )
        },
        contentAlignment = Alignment.Center
    ) { content ->
        Text(content, style = MaterialTheme.typography.Display2, color = Primary, textAlign = TextAlign.Center)
    }
}


