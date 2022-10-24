package orot.apps.smartcounselor.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Gray20
import orot.apps.smartcounselor.presentation.app_style.Pretendard
import orot.apps.smartcounselor.presentation.app_style.White
import orot.apps.sognora_compose_extension.nav_controller.popUpToTop
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun MagoAppBar(
    navController: NavController, mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    navController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
        val mod = Modifier
            .padding(horizontal = 20.dp)
            .size(40.dp, 30.dp)
            .background(Gray20)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Home.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Start)
                        }
                    }),
                    text = "1",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Guide.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
                        }
                    }),
                    text = "2",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Conversation.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Empty)
                        }
                    }),
                    text = "3",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Conversation.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Conversation)
                        }
                    }),
                    text = "3-1",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.BloodPressure.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Empty)
                        }
                    }),
                    text = "4",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Conversation.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
                        }
                    }),
                    text = "4-1",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.Conversation.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.RetryAndChat)
                        }
                    }),
                    text = "5",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.ChatList.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Retry)
                        }
                    }),
                    text = "6",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
                Text(
                    modifier = mod.then(Modifier.clickable {
                        navController.navigate(Screens.ChatList.route) {
                            popUpToTop(navController)
                            mainViewModel.updateBottomMenu(BottomMenu.Call)
                        }
                    }),
                    text = "7",
                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
                )
            }

            route.takeIf { it != "home" }?.run {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
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

                    TimeContent()
                }
            }
        }
    }
}

@Composable
private fun TimeContent(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    mainViewModel.currentTime.collectAsState().value.also { currentTime ->
        val date = currentTime.split("-")[0]
        val hour = currentTime.split("-")[1].split(":")[0].toInt()
        val minute = currentTime.split("-")[1].split(":")[1].toInt()
        val yoil = currentTime.split("-")[2]

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W600,
                    fontSize = 40.sp,
                )
            ) {
                append("${String.format("%02d", hour)}:${String.format("%02d", minute)}")
            }
            withStyle(
                style = SpanStyle(
                    color = White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W600,
                    fontSize = 26.sp,
                )
            ) {
                append(hour.takeIf { it >= 12 }?.run { "PM" } ?: "AM")
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Row {
                Text(text = annotatedString)
            }
            Text(
                text = "$date($yoil)",
                style = MaterialTheme.typography.subtitle1,
                color = White,
            )
        }
    }


}