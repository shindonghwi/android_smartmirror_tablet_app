package orot.apps.smartcounselor.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import orot.apps.smartcounselor.MagoActivity
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.blood_pressure.BloodPressureScreen
import orot.apps.smartcounselor.presentation.chat_list.ChatListScreen
import orot.apps.smartcounselor.presentation.conversation.ConversationScreen
import orot.apps.smartcounselor.presentation.guide.GuideScreen
import orot.apps.smartcounselor.presentation.home.HomeScreen
import orot.apps.smartcounselor.presentation.server_connection_fail.ServerConnectionFailScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph() {
    val navController = navigationKit.navHostController

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen()
        }
        composable(route = Screens.Guide.route) {
            GuideScreen()
        }
        composable(route = Screens.Conversation.route) {
            ConversationScreen()
        }
        composable(route = Screens.BloodPressure.route) {
            BloodPressureScreen()
        }
        composable(route = Screens.ChatList.route) {
            ChatListScreen()
        }
        composable(route = Screens.ServerConnectionFailScreen.route) {
            ServerConnectionFailScreen()
        }
    }
}