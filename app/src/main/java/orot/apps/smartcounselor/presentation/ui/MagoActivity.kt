package orot.apps.smartcounselor.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import orot.apps.smartcounselor.graph.NavGraph
import orot.apps.smartcounselor.graph.NavigationKit
import orot.apps.smartcounselor.presentation.components.appbar.MagoAppBar
import orot.apps.smartcounselor.presentation.components.bottombar.MagoBottomBar
import orot.apps.smartcounselor.presentation.style.Black
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.SmartCounselorTheme
import orot.apps.smartcounselor.presentation.ui.screens.sheet.account_register.AccountRegisterSheetContent
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.RecommendationSheetContent
import orot.apps.smartcounselor.presentation.ui.utils.modifier.backgroundVGradient
import orot.apps.smartcounselor.presentation.ui.utils.permission.CheckPermission
import orot.apps.systems.hideSystemUI


@AndroidEntryPoint
class MagoActivity : ComponentActivity() {

    var mainViewModel = viewModels<MainViewModel>()

    private val PERMISSION_RECODE_AUDIO = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContent {
            hiltViewModel<MainViewModel>()
            mainViewModel.value.navigationKit = NavigationKit(rememberNavController())
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
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
            Column(modifier = Modifier.fillMaxSize()) {
                MagoAppBar()
                Box(modifier = Modifier.weight(1f)) {
                    NavGraph()
                }
                MagoBottomBar()
            }
            AccountRegisterSheetContent()
            RecommendationSheetContent()
        }
    }
}

