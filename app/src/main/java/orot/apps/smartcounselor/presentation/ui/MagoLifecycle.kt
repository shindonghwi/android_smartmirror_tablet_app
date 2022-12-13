package orot.apps.smartcounselor.presentation.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG

@Composable
fun MagoLifecycle() {

    val context = LocalContext.current
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    Log.w("VIEWMODEL", "MagoLifecycle: $mainViewModel")

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                mainViewModel.initTTS(context)
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "MagoLifecycle: ON_PAUSE")
                mainViewModel.changeMicState(false)
                mainViewModel.pauseTts()
            }
            else -> Log.d(TAG, "MagoLifecycle: ON_ANY: $event")
        }
    }
}

@Composable
private fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}