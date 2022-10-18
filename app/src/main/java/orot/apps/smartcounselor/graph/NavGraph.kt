package orot.apps.smartcounselor.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.blood_pressure.BloodPressureScreen
import orot.apps.smartcounselor.presentation.chat_list.ChatListScreen
import orot.apps.smartcounselor.presentation.conversation.ConversationScreen
import orot.apps.smartcounselor.presentation.guide.GuideScreen
import orot.apps.smartcounselor.presentation.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    )
    {
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screens.Guide.route) {
            GuideScreen(navController)
        }
        composable(route = Screens.Conversation.route) {
            ConversationScreen(navController)
        }
        composable(route = Screens.BloodPressure.route) {
            BloodPressureScreen(navController)
        }
        composable(route = Screens.ChatList.route) {
            ChatListScreen(navController)
        }
    }
}