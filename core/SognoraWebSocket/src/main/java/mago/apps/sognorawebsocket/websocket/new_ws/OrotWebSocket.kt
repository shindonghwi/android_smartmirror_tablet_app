package mago.apps.sognorawebsocket.websocket.new_ws

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import mago.apps.sognorawebsocket.websocket.model.ServerState
import mago.apps.sognorawebsocket.websocket.model.callback.IActionCallback
import okhttp3.*
import okio.ByteString.Companion.toByteString

class OrotWebSocket : IOrotWebSocket {

    val TAG = "OrotWebSocket"

    private var webSocket: WebSocket? = null
    private var request: Request? = null
    private var client: OkHttpClient? = null
    private var orotMessageParseHelper: OrotMessageParseHelper? = null
    var serverState = MutableStateFlow<ServerState>(ServerState.Idle)
    private var iActionCallback: IActionCallback? = null

    /** 웹 소켓 연결하기 */
    override fun initWebSocket(url: String) {
        try {
            orotMessageParseHelper = OrotMessageParseHelper(iActionCallback)
            request = Request.Builder().url(url).build()
            client = OkHttpClient()
        } catch (e: Exception) {
            Log.e(TAG, "initWebSocket: ${e.message}")
            return
        }

        request?.run {
            try {
                webSocket = client!!.newWebSocket(this, listener)
            } catch (e: Exception) {
                serverState.value = ServerState.Error
            }
        }
    }

    override fun sendBuffer(buf: ByteArray, bufferSize: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                webSocket?.send(buf.toByteString(0, bufferSize))
            } catch (e: Exception) {
                Log.e(TAG, "sendBuffer Error: ${e.message}")
            }
        }
    }

    override fun sendMsg(msg: String) {
        try {
            webSocket?.send(msg)
        } catch (e: Exception) {
            Log.e(TAG, "sendMsg Error: ${e.message}")
        }
    }

    override fun setActionCallback(callback: IActionCallback) {
        iActionCallback = callback
    }

    override fun close() {
        orotMessageParseHelper = null
        request = null
        client = null
        webSocket?.cancel()
        webSocket = null
    }

    override fun currentServerState() = serverState

    private var listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            iActionCallback?.onStartDialog()
            serverState.value = ServerState.Opened
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            orotMessageParseHelper?.parse(text)
            serverState.value = ServerState.Connected
        }

        override fun onFailure(
            webSocket: WebSocket, t: Throwable, response: Response?
        ) {
            super.onFailure(webSocket, t, response)
            serverState.value = ServerState.Error
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            serverState.value = ServerState.Error
        }
    }


}