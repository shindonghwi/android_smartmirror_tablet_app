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
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "MagoLifecycle: ON_RESUME")
            }
            Lifecycle.Event.ON_CREATE -> {
                mainViewModel.initTTS(context)
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

//
//@Composable
//private fun WebSocketState() {
//
//    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
//    val state = mainViewModel.getWebSocketState().collectAsState().value
//
//    Log.w("VIEWMODEL", "WebSocketState: $mainViewModel")
//    Log.d(TAG, "WebSocketState: $state")
//
//    LaunchedEffect(key1 = state) {
//        when (state) {
//            is WebSocketState.Connected -> {
//                navigationKit.clearAndMove(Screens.Conversation.route) {
//                    mainViewModel.run {
//                        Log.w("@@@@@@@@@@", "WebSocketState mainViewModel: ${mainViewModel}")
//                        updateBottomMenu(BottomMenu.Conversation)
////                        startAudioRecorder()
//                    }
//                }
//            }
//            is WebSocketState.Failed -> {
//                navigationKit.clearAndMove(Screens.ServerConnectionFailScreen.route) {
//                    mainViewModel.updateBottomMenu(BottomMenu.ServerRetry)
//                }
//            }
//            is WebSocketState.Idle -> {
//                navigationKit.clearAndMove(Screens.Home.route) {
//                    mainViewModel.updateBottomMenu(BottomMenu.Start)
//                }
//            }
//            else -> {
//                Log.d(TAG, "WebSocketState ELSE: $state")
//            }
//        }
//    }
//}
