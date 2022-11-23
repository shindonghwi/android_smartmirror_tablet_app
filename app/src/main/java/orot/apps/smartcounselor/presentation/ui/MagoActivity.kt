package orot.apps.smartcounselor.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import orot.apps.smartcounselor.graph.NavGraph
import orot.apps.smartcounselor.graph.NavigationKit
import orot.apps.smartcounselor.presentation.components.appbar.MagoAppBar
import orot.apps.smartcounselor.presentation.components.bottombar.MagoBottomBar
import orot.apps.smartcounselor.presentation.style.Black
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.SmartCounselorTheme
import orot.apps.smartcounselor.presentation.ui.utils.modifier.backgroundVGradient
import orot.apps.smartcounselor.presentation.ui.utils.permission.CheckPermission
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.getViewModel
import orot.apps.systems.hideSystemUI


@AndroidEntryPoint
class MagoActivity : ComponentActivity() {

    val mainViewModel by viewModels<MainViewModel>()

    private val PERMISSION_RECODE_AUDIO = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContent {
            navigationKit = NavigationKit(rememberNavController())
            CheckPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_RECODE_AUDIO)
            MagoHCApp()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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
            Scaffold(backgroundColor = Color.Transparent,
                topBar = { MagoAppBar() },
                bottomBar = { MagoBottomBar() }) {
                Box(modifier = Modifier.padding(paddingValues = it)) {
                    NavGraph()
                }
            }
        }
    }
}
