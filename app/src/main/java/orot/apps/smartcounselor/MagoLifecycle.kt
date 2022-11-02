package orot.apps.smartcounselor

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import orot.apps.smartcounselor.MagoActivity.Companion.TAG
import orot.apps.sognora_compose_extension.lifecycle.OnLifecycleEvent

@Composable
fun MagoLifecycle() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "MagoLifecycle: ON_RESUME")
            }
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "MagoLifecycle: ON_CREATE")
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