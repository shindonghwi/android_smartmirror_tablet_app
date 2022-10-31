package orot.apps.smartcounselor

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import orot.apps.sognora_compose_extension.lifecycle.OnLifecycleEvent
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO

@Composable
fun MagoLifecycle(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val context = LocalContext.current
    val TAG = "Mago Lifecycle"
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "MagoLifecycle: ON_RESUME")
                mainViewModel.sognoraTts.createTts(context)
            }
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "MagoLifecycle: ON_CREATE")
                coroutineScopeOnIO {
                    mainViewModel.setGuideTtsUrlList()
                }
            }
            Lifecycle.Event.ON_DESTROY -> Log.d(TAG, "MagoLifecycle: ON_DESTROY")
            Lifecycle.Event.ON_START -> Log.d(TAG, "MagoLifecycle: ON_START")
            Lifecycle.Event.ON_STOP -> Log.d(TAG, "MagoLifecycle: ON_STOP")
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "MagoLifecycle: ON_PAUSE")
                mainViewModel.changeSendingStateAudioBuffer(false)
                mainViewModel.stopGoogleTts()
            }
            else -> Log.d(TAG, "MagoLifecycle: ON_ANY")
        }
    }
}