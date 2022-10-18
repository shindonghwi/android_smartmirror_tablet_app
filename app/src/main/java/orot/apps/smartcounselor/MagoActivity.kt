package orot.apps.smartcounselor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import orot.apps.resources.Black
import orot.apps.resources.Black80
import orot.apps.smartcounselor.graph.NavGraph
import orot.apps.smartcounselor.presentation.app_style.SmartCounselorTheme
import orot.apps.smartcounselor.presentation.components.MagoAppBar
import orot.apps.smartcounselor.presentation.components.MagoBottomBar
import orot.apps.sognora_compose_extension.gradient.backgroundVGradient

@AndroidEntryPoint
class MagoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagoHCApp()
        }
    }
}

@Composable
private fun MagoHCApp() {
    SmartCounselorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundVGradient(listOf(Black80, Black))
        ) {
            val navController = rememberNavController()
            Scaffold(
                backgroundColor = Color.Transparent,
                topBar = { MagoAppBar(navController = navController) },
                bottomBar = { MagoBottomBar(navController = navController) }
            ) {
                Box(modifier = Modifier.padding(paddingValues = it)) {
                    NavGraph(navController = navController)
                }
            }
        }

    }
}
