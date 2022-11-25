package orot.apps.smartcounselor.graph

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.BloodPressureScreen
import orot.apps.smartcounselor.presentation.ui.screens.chat_list.ChatListScreen
import orot.apps.smartcounselor.presentation.ui.screens.conversation.ConversationScreen
import orot.apps.smartcounselor.presentation.ui.screens.guide.GuideScreen
import orot.apps.smartcounselor.presentation.ui.screens.home.HomeScreen
import orot.apps.smartcounselor.presentation.ui.screens.server_connection_fail.ServerConnectionFailScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph() {

    var waitTime = 0L

    val activity = LocalContext.current as MagoActivity
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val navController = mainViewModel.navigationKit.navHostController

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

    BackHandler(enabled = true) {
        if(System.currentTimeMillis() - waitTime >=1500 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(activity,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            activity.finish() // 액티비티 종료
        }
    }
}