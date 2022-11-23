package orot.apps.smartcounselor.graph

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

class NavigationKit(
    val navHostController: NavHostController
) {
    fun clearAndMove(nextPage: String, postEvent: (() -> Unit)? = null) {
        navHostController.navigate(nextPage) {
            popUpToTop(navHostController)
            postEvent?.let { it() }
        }
    }

    private fun NavOptionsBuilder.popUpToTop(navController: NavController) {
        popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
            inclusive = true
        }
    }
}