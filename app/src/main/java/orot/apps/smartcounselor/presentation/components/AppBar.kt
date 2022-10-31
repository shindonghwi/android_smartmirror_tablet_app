package orot.apps.smartcounselor.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Gray20
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun MagoAppBar(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val controller = navigationKit.navHostController
    controller.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
//        val mod = Modifier
//            .padding(horizontal = 20.dp)
//            .size(40.dp, 30.dp)
//            .background(Gray20)
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Home.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Start)
//                        }
//                    }),
//                    text = "1",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Guide.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
//                        }
//                    }),
//                    text = "2",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Conversation.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Empty)
//                        }
//                    }),
//                    text = "3",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Conversation.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Conversation)
//                        }
//                    }),
//                    text = "3-1",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.BloodPressure.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Empty)
//                        }
//                    }),
//                    text = "4",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Conversation.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
//                        }
//                    }),
//                    text = "4-1",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.Conversation.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.RetryAndChat)
//                        }
//                    }),
//                    text = "5",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.ChatList.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Retry)
//                        }
//                    }),
//                    text = "6",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//                Text(
//                    modifier = mod.then(Modifier.clickable {
//                        navigationKit.clearAndMove(Screens.ChatList.route) {
//                            mainViewModel.updateBottomMenu(BottomMenu.Call)
//                        }
//                    }),
//                    text = "7",
//                    style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center),
//                )
//            }

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
//}
