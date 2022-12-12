//package mago.apps.sognorawebsocket.websocket
//
//import android.util.Log
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import mago.apps.sognorawebsocket.websocket.model.WebSocketState
//import okhttp3.*
//import okio.ByteString
//import okio.ByteString.Companion.toByteString
//import javax.inject.Inject
//
//class SognoraWebSocketImpl @Inject constructor() : SognoraWebSocket {
//
//    val TAG = "SognoraWebSocket"
//
//    var webSocket: WebSocket? = null
//    private var request: Request? = null
//    private var client: OkHttpClient? = null
//    lateinit var listener: SognoraWebSocketListener
//    var webSocketState = MutableStateFlow<WebSocketState>(WebSocketState.Idle)
//
//    /** 웹 소켓 연결하기 */
//    override fun initWebSocket(url: String) {
//        Log.w(TAG, "initWebSocket: $url")
//        try {
//            request = Request.Builder().url(url).build()
//            client = OkHttpClient()
//        } catch (e: Exception) {
//            Log.e(TAG, "initWebSocket: ${e.message}")
//            return
//        }
//
//        request?.run {
//            try {
//                webSocket = client!!.newWebSocket(this, object : WebSocketListener() {
//                    override fun onOpen(webSocket: WebSocket, response: Response) {
//                        super.onOpen(webSocket, response)
//                        listener.open(response)
//                        webSocketState.value = WebSocketState.Connected
//                    }
//
//                    override fun onMessage(webSocket: WebSocket, text: String) {
//                        super.onMessage(webSocket, text)
//                        listener.onMessageText(text)
//                    }
//
//                    override fun onFailure(
//                        webSocket: WebSocket, t: Throwable, response: Response?
//                    ) {
//                        super.onFailure(webSocket, t, response)
//                        Log.e("asdkj;asd;jklasjads", "onFailure: ${response?.code}", )
//                        Log.e("asdkj;asd;jklasjads", "onFailure: ${response?.message}", )
//                        Log.e("asdkj;asd;jklasjads", "onFailure: ${response?.body}", )
//                        Log.e("asdkj;asd;jklasjads", "onFailure: ${t.message}", )
//                        Log.e("asdkj;asd;jklasjads", "onFailure: ${t.stackTrace}", )
//                        listener.fail(response, t)
//                        webSocketState.value = WebSocketState.Failed
//                    }
//
//                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                        super.onClosed(webSocket, code, reason)
//                        listener.close(code, reason)
//                        webSocketState.value = WebSocketState.DisConnected
//                    }
//
//                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                        super.onClosing(webSocket, code, reason)
//                        listener.onClosing(code, reason)
//                        webSocketState.value = WebSocketState.Closing
//                    }
//
//                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                        super.onMessage(webSocket, bytes)
//                        listener.onMessageByteString(bytes)
//                    }
//                })
//            } catch (e: Exception) {
//                Log.e(TAG, "initWebSocket Error: ${e.message}")
//            }
//        }
//    }
//
//    override fun sendBuffer(buf: ByteArray, bufferSize: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                webSocket?.send(buf.toByteString(0, bufferSize))
//            } catch (e: Exception) {
//                Log.e(TAG, "sendBuffer Error: ${e.message}")
//            }
//        }
//    }
//
//    override fun sendMsg(msg: String) {
//        try {
//            webSocket?.send(msg)
//        } catch (e: Exception) {
//            Log.e(TAG, "sendMsg Error: ${e.message}")
//        }
//    }
//
//    override fun setWebSocketListener(listener: SognoraWebSocketListener) {
//        this.listener = listener
//    }
//
//    override fun getWebSocketState(): StateFlow<WebSocketState> {
//        return webSocketState
//    }
//
//    override fun close() {
//        webSocketState.value = WebSocketState.Idle
//        webSocket?.cancel()
//        request = null
//        webSocket = null
//    }
//
//}