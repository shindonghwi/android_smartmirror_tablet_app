package orot.apps.smartcounselor.presentation.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import orot.apps.smartcounselor.BuildConfig
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.model.local.BuildShowMode
import orot.apps.smartcounselor.presentation.style.*

@Composable
fun HomeScreen() {

    val configuration = LocalConfiguration.current
    val logoWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.70f }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
        constraintSet = homeScreenConstraintSet(),
    ) {
        Image(
            modifier = Modifier
                .layoutId("description")
                .widthIn(min = logoWidth),
            painter = painterResource(id = R.drawable.mago_full_logo_white),
            contentDescription = "mago-logo",
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun homeScreenConstraintSet() = ConstraintSet {
    val description = createRefFor("description")

    constrain(description) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.3f)
    }
}