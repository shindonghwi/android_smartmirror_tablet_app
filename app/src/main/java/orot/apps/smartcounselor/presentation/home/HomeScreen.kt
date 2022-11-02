package orot.apps.smartcounselor.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {

    val configuration = LocalConfiguration.current
    val logoWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.70f }

    Log.d("ASdasdasd", "HomeScreen: ${mainViewModel.bloodPressureMin}")

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