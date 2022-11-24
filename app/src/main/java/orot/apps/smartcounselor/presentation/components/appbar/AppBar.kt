package orot.apps.smartcounselor.presentation.components.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import orot.apps.smartcounselor.R
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
                Image(
                    modifier = Modifier
                        .weight(0.25f)
                        .padding(top = 10.dp),
                    painter = painterResource(id = R.drawable.mago_full_logo_white),
                    contentDescription = "mago-logo",
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.weight(0.33f))
            }
        }
    }
}
