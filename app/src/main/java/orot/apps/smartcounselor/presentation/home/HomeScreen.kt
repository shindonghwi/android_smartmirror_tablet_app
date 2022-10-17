package orot.apps.smartcounselor.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import orot.apps.resources.Primary
import orot.apps.resources.White
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Display1
import orot.apps.sognora_compose_extension.animation.clickBounce

@Composable
fun HomeScreen(navController: NavController) {

    val configuration = LocalConfiguration.current
    val logoWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.72f }
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
        constraintSet = homeScreenConstraintSet(),
    ) {
        Image(
            modifier = Modifier
                .layoutId("description")
                .widthIn(min = logoWidth),
            painter = painterResource(id = orot.apps.resources.R.drawable.mago_full_logo_white),
            contentDescription = "mago-logo",
            contentScale = ContentScale.FillWidth
        )

        Box(modifier = Modifier
            .layoutId("startButton")
            .clickBounce { navController.navigate(Screens.Guide.route) }
            .sizeIn(minWidth = startWidth, minHeight = startWidth / 2)
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
            Text(text = "시작", style = MaterialTheme.typography.Display1, color = White)
        }
    }
}

@Composable
private fun homeScreenConstraintSet() = ConstraintSet {
    val description = createRefFor("description")
    val startButton = createRefFor("startButton")

    constrain(description) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.2f)
    }

    constrain(startButton) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.90f)
    }
}