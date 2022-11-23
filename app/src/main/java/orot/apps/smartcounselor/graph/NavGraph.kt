package orot.apps.smartcounselor.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.BloodPressureScreen
import orot.apps.smartcounselor.presentation.ui.screens.chat_list.ChatListScreen
import orot.apps.smartcounselor.presentation.ui.screens.conversation.ConversationScreen
import orot.apps.smartcounselor.presentation.ui.screens.guide.GuideScreen
import orot.apps.smartcounselor.presentation.ui.screens.home.HomeScreen
import orot.apps.smartcounselor.presentation.ui.screens.server_connection_fail.ServerConnectionFailScreen

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