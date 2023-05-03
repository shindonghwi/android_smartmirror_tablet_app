package orot.apps.smartcounselor.presentation.components.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import orot.apps.smartcounselor.BuildConfig
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.model.local.BuildShowMode
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.style.White
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun MagoAppBar() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val controller = mainViewModel.navigationKit.navHostController
    controller.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
        route.takeIf { it != "home" }?.run {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "대한민국 대표 음성 의료서비스",
                        style = MaterialTheme.typography.h3,
                        color = White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Smart Counselor",
                        style = MaterialTheme.typography.body1,
                        color = White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
