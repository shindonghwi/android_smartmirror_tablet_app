package orot.apps.smartcounselor.graph

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnMain

class NavigationKit(
    val navHostController: NavHostController
) {
    fun clearAndMove(nextPage: String, postEvent: (() -> Unit)? = null) {
        coroutineScopeOnMain {
            navHostController.navigate(nextPage) {
                popUpToTop(navHostController)
                postEvent?.let { it() }
            }
        }
    }

    private fun NavOptionsBuilder.popUpToTop(navController: NavController) {
        coroutineScopeOnMain {
            popUpTo(navController.currentBackStackEntry?.destination?.route ?: return@coroutineScopeOnMain) {
                inclusive = true
            }
        }
    }
}