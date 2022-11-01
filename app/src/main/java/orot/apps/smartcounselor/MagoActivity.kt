package orot.apps.smartcounselor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import orot.apps.smartcounselor.graph.NavGraph
import orot.apps.smartcounselor.presentation.app_style.Black
import orot.apps.smartcounselor.presentation.app_style.Black80
import orot.apps.smartcounselor.presentation.app_style.SmartCounselorTheme
import orot.apps.smartcounselor.presentation.components.MagoAppBar
import orot.apps.smartcounselor.presentation.components.MagoBottomBar
import orot.apps.sognora_compose_extension.gradient.backgroundVGradient
import orot.apps.sognora_compose_extension.nav_controller.NavigationKit
import orot.apps.sognora_compose_extension.permission.CheckPermission
import orot.apps.systems.hideSystemUI


@AndroidEntryPoint
class MagoActivity : ComponentActivity() {

    private val PERMISSION_RECODE_AUDIO = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navigationKit = NavigationKit(rememberNavController())

            CheckPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_RECODE_AUDIO)
            MagoHCApp()
        }
        hideSystemUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_RECODE_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "오디오 녹음 권한이 필요해요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        val TAG = "MagoApplication"
        lateinit var navigationKit: NavigationKit
    }
}

@Composable
private fun MagoHCApp() {
    SmartCounselorTheme {

        MagoLifecycle()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundVGradient(listOf(Black80, Black))
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Scaffold(
                backgroundColor = Color.Transparent,
                topBar = { MagoAppBar() },
                bottomBar = { MagoBottomBar() }
            ) {
                Box(modifier = Modifier.padding(paddingValues = it)) {
                    NavGraph()
                }
            }
        }
    }
}
